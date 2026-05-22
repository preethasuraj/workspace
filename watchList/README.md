# WatchList — Real-Time Price Tracker

An Android app to search financial instruments, manage a watchlist, and see real-time
price updates. Built with Kotlin, Jetpack Compose, Coroutines/Flow, Hilt, Room, and
Finnhub (REST for search/snapshots, WebSocket for live trades).

## Setup

1. **Get a Finnhub API key** — register for a free key at https://finnhub.io/register.
2. **Add the key** to `local.properties` in the project root (this file is gitignored, so
   the key never enters version control):

   ```properties
   FINNHUB_API_KEY=your_key_here
   ```

   It's exposed at build time as `BuildConfig.FINNHUB_API_KEY`.
3. **Build & run** in Android Studio (JDK 17; Android SDK 36). Or from the CLI:
   `./gradlew installDebug`.

Toolchain: AGP 9.1.1, Gradle 9.3.1, Kotlin 2.2.20, KSP 2.3.4, Compose BOM 2026.05,
Hilt 2.59.2, Room 2.8.4, Retrofit 3 + OkHttp 5 + kotlinx.serialization, minSdk 24.

## Demo / fake-data mode

Demo mode runs the full experience with **no Finnhub dependency** — no API key, network,
market hours, or rate limits. It swaps in a `FakeMarketDataSource`: ~12 well-known tickers
(AAPL, MSFT, NVDA, …) that are searchable, addable, and stream a synthetic random-walk
price every ~750ms. You'll see prices update live, green/red movement, the two-line
price/change row, and a steady "Connected" state — exactly the real experience, offline.

Enable it any of these ways:

- **Android Studio (easiest):** just leave `FINNHUB_API_KEY` unset/blank in
  `local.properties` — demo mode is selected automatically, so the app runs out of the box.
- **Android Studio (explicit):** add `useFakeData=true` to `gradle.properties`, then Run.
  This forces demo mode even if a key is present.
- **Command line:** `./gradlew installDebug -PuseFakeData=true`.

With a key set and the flag off, the app uses live Finnhub data. The selection lives in
`DataSourceModule`, driven by `BuildConfig.USE_FAKE_DATA` (the `useFakeData` Gradle
property) with an automatic fallback to fake when the key is blank.

## What it does

Search for US stocks, add/remove them from a watchlist that persists across launches, and
see the latest known price per item with live updates while the app is open. The UI
surfaces loading, empty, error, stale-data, and connecting/reconnecting states.

## Architecture

Single module, layered by package with a strict `presentation → domain → data` dependency
direction. The domain layer is free of Android and framework types.

**Data layer.** Retrofit + kotlinx.serialization for REST (`/search`, `/quote`); an OkHttp
`WebSocket` (`WebSocketManager`) for the trade stream; Room for the watchlist. The key
abstraction is the `MarketDataSource` interface — the seam that lets a fake/demo
implementation (`FakeMarketDataSource`) replace the real Finnhub one for offline review and
tests. `PriceCache` is
an in-memory, latest-price-per-symbol map.

**Domain layer.** Plain models (`Instrument`, `Quote`, `PricePoint`, `WatchlistItem`,
`ConnectionState`) and the `WatchlistRepository` interface that the presentation layer
depends on.

**Presentation layer.** MVVM. Each screen exposes one immutable `StateFlow<UiState>`;
Compose collects it with `collectAsStateWithLifecycle()`. ViewModels are provided by Hilt.

**The core idea.** The watchlist (slow, persisted in Room) and prices (volatile: a one-shot
REST snapshot followed by a continuous WebSocket feed held in `PriceCache`) are two
different data sources merged into one observable stream. The repository does:

```
combine(dao.observeAll(), priceCache.prices, connectionState) -> List<WatchlistItem>
```

Adding a symbol persists it, fetches a REST snapshot to seed price + previous close, and
the WebSocket subscribes to it; live ticks then flow into the cache and the merged list
updates. Staleness is derived from connection state (see tradeoffs).

## States handled

Loading, empty, and error (with retry) on search; loading/empty/content on the watchlist;
per-item missing-price ("—"), connection banner (connecting / reconnecting with attempt /
offline), and per-row staleness when the live feed isn't delivering. The WebSocket
reconnects with exponential backoff + jitter and re-subscribes the full symbol set on each
(re)open.

## Key design decisions & tradeoffs

- **US stocks** were chosen for their clean REST `/search` and `/quote` endpoints. The cost
  is that live trades only stream during US market hours; the demo/fake mode (see above)
  covers off-hours review.
- **Symbol is the canonical identity** — it's the WebSocket subscription key and the Room
  primary key. Finnhub search returns multiple rows per symbol (exchanges/share classes),
  so results are collapsed to one row per symbol with `distinctBy`. Two rows with the same
  symbol resolve to the same quote/stream, so this doesn't lose price fidelity.
- **Staleness is connection-driven, not trade-timestamp driven.** A symbol that simply
  isn't trading is *not* stale — its last price is still the market price. A row is stale
  only when we hold a price but the feed isn't currently delivering (connecting /
  reconnecting / offline). This avoids false "stale" flags on quiet symbols.
- **kotlinx.serialization** over Gson/Moshi — compile-time, reflection-free, and
  Kotlin-aware (respects non-null types and default values, which back the DTO robustness).
- **No injected dispatcher in the data layer** — Room and Retrofit suspend functions are
  already main-safe, so an explicit IO hop would be redundant.
- **Single module with enforced package layering** rather than multi-module — appropriate
  for this scope; a growth path to `:core` / `:feature` modules is the obvious next step if
  it scaled.
- **Separate search screen** (instrument discovery) vs. the watchlist — search queries the
  whole Finnhub universe to *add* instruments, which is a different intent from filtering
  the existing list, so combining them into one search bar would overload it.
- **Connection lifecycle: always-on while the watchlist is non-empty.** The socket opens on
  the first subscription and closes when the last symbol is removed, staying open for the
  app process's lifetime otherwise. This was a deliberate simplification over a
  demand-/foreground-scoped connection (which is listed as a future enhancement).

## Assumptions & limitations

- **Finnhub free tier** throttles WebSocket connection frequency; rapid reconnects or
  repeated app relaunches during development can trip **HTTP 429**, after which connections
  are refused for a cooldown. The client backs off harder on 429, but you may need to wait
  a minute before it reconnects.
- **Live trades require US market hours.** Off-hours the stream is connected but silent;
  rows show last-known prices (the planned demo mode addresses this for reviewers).
- **Currency is assumed USD** — Finnhub's quote endpoint doesn't return a currency, and the
  app is scoped to US stocks.
- **Previous close** is seeded from the snapshot (a per-day constant). If the app stays open
  across a session/day boundary it can become stale until re-snapshotted; pull-to-refresh
  would address this.
- The socket is **not foreground/lifecycle scoped** — it remains open while the app process
  lives and the watchlist is non-empty.

## Not yet implemented

- **Unit tests** — planned coverage: repository merge, connection-driven staleness,
  reconnect/backoff, ViewModel state transitions, and search debounce/`flatMapLatest`.

## AI / tooling assistance

This project was built with AI assistance (Claude) used for: the initial architecture and
design document, scaffolding the layers, and iterative debugging of toolchain and runtime
issues — notably AGP 9 built-in-Kotlin + KSP compatibility, the `hiltViewModel` API
migration, and the WebSocket connection lifecycle (idle timeouts, reconnect/backoff, and
Finnhub free-tier 429 throttling). All design decisions and tradeoffs were reviewed and
chosen deliberately.
