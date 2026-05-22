package com.preethasuraj.watchlist.data.source

import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.PricePoint
import com.preethasuraj.watchlist.domain.model.Quote
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Offline [MarketDataSource] for the demo mode: a fixed instrument universe, REST-like
 * snapshots, and a synthetic random-walk price stream. Lets the full live experience run
 * with no Finnhub dependency, network, market hours, or rate limits. Connection state is
 * always [ConnectionState.Connected].
 */
@Singleton
class FakeMarketDataSource @Inject constructor() : MarketDataSource {

    private data class FakeInstrument(val name: String, val basePrice: Double)

    private val universe: Map<String, FakeInstrument> = linkedMapOf(
        "AAPL" to FakeInstrument("Apple Inc", 195.0),
        "MSFT" to FakeInstrument("Microsoft Corp", 415.0),
        "GOOGL" to FakeInstrument("Alphabet Inc", 175.0),
        "AMZN" to FakeInstrument("Amazon.com Inc", 185.0),
        "NVDA" to FakeInstrument("NVIDIA Corp", 120.0),
        "META" to FakeInstrument("Meta Platforms Inc", 500.0),
        "TSLA" to FakeInstrument("Tesla Inc", 250.0),
        "NFLX" to FakeInstrument("Netflix Inc", 640.0),
        "AMD" to FakeInstrument("Advanced Micro Devices", 160.0),
        "INTC" to FakeInstrument("Intel Corp", 30.0),
        "JPM" to FakeInstrument("JPMorgan Chase & Co", 205.0),
        "DIS" to FakeInstrument("Walt Disney Co", 100.0),
    )

    /** Current (walking) price per symbol; previous close is the base price. */
    private val prices = ConcurrentHashMap<String, Double>()
    private val subscribed = MutableStateFlow<Set<String>>(emptySet())

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Connected)
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    override suspend fun search(query: String): Result<List<Instrument>> {
        delay(SEARCH_LATENCY_MS) // mimic network so the loading state is visible
        val trimmedQuery = query.trim()
        val matches = universe
            .filter { (symbol, info) ->
                symbol.contains(trimmedQuery, ignoreCase = true) ||
                    info.name.contains(trimmedQuery, ignoreCase = true)
            }
            .map { (symbol, info) -> Instrument(symbol, info.name, "Common Stock") }
        return Result.success(matches)
    }

    override suspend fun snapshot(symbol: String): Result<Quote?> {
        val info = universe[symbol] ?: return Result.success(null)
        val current = priceFor(symbol)
        return Result.success(
            Quote(
                symbol = symbol,
                price = current,
                previousClose = info.basePrice,
                updatedAt = System.currentTimeMillis(),
            ),
        )
    }

    override fun priceStream(): Flow<PricePoint> = flow {
        while (true) {
            delay(TICK_MS)
            val now = System.currentTimeMillis()
            subscribed.value.forEach { symbol ->
                val next = (priceFor(symbol) * (1 + Random.nextDouble(-MAX_STEP, MAX_STEP)))
                    .coerceAtLeast(0.01)
                prices[symbol] = next
                emit(PricePoint(symbol, next, now))
            }
        }
    }

    override fun subscribe(symbol: String) {
        subscribed.update { it + symbol }
    }

    override fun unsubscribe(symbol: String) {
        subscribed.update { it - symbol }
    }

    private fun priceFor(symbol: String): Double =
        prices.getOrPut(symbol) { universe[symbol]?.basePrice ?: DEFAULT_PRICE }

    private companion object {
        const val TICK_MS = 1500L
        const val MAX_STEP = 0.004 // ±0.4% per tick
        const val SEARCH_LATENCY_MS = 200L
        const val DEFAULT_PRICE = 100.0
    }
}
