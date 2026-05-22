package com.preethasuraj.watchlist.data.repository

import com.preethasuraj.watchlist.data.local.WatchedInstrumentEntity
import com.preethasuraj.watchlist.data.local.WatchlistDao
import com.preethasuraj.watchlist.data.local.toEntity
import com.preethasuraj.watchlist.data.source.MarketDataSource
import com.preethasuraj.watchlist.di.ApplicationScope
import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.PriceMovement
import com.preethasuraj.watchlist.domain.model.Quote
import com.preethasuraj.watchlist.domain.model.WatchlistItem
import com.preethasuraj.watchlist.domain.repository.WatchlistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Combines the persisted watchlist (Room) with the in-memory [PriceCache] to produce the
 * live UI list. Two long-lived collectors feed the cache from the stream and keep the
 * stream's subscriptions aligned with the saved symbols (which also drives when the socket
 * is open: it stays connected while at least one symbol is watched). Room/Retrofit suspend
 * calls are main-safe, so no dispatcher hop is needed.
 */
@Singleton
class WatchlistRepositoryImpl @Inject constructor(
    private val dao: WatchlistDao,
    private val marketDataSource: MarketDataSource,
    private val priceCache: PriceCache,
    @ApplicationScope private val scope: CoroutineScope,
) : WatchlistRepository {

    init {
        // Pipe live ticks into the in-memory cache.
        scope.launch {
            marketDataSource.priceStream().collect { priceCache.update(it) }
        }
        // Keep WS subscriptions aligned with the persisted symbols (also covers startup:
        // the first emission subscribes everything already saved).
        scope.launch {
            var subscribed = emptySet<String>()
            dao.observeSymbols()
                .map { it.toSet() }
                .distinctUntilChanged()
                .collect { symbols ->
                    (symbols - subscribed).forEach { symbol ->
                        marketDataSource.subscribe(symbol)
                        launch { seedSnapshot(symbol) }
                    }
                    (subscribed - symbols).forEach {
                        marketDataSource.unsubscribe(it)
                        priceCache.remove(it)
                    }
                    subscribed = symbols
                }
        }
    }

    override val connectionState: StateFlow<ConnectionState> = marketDataSource.connectionState

    override suspend fun search(query: String): Result<List<Instrument>> =
        marketDataSource.search(query)

    override fun observeWatchedSymbols(): Flow<Set<String>> =
        dao.observeSymbols().map { it.toSet() }

    override fun observeWatchlist(): Flow<List<WatchlistItem>> =
        combine(
            dao.observeAll(),
            priceCache.prices,
            marketDataSource.connectionState,
        ) { entities, prices, connection ->
            val live = connection is ConnectionState.Connected
            entities.map { it.toWatchlistItem(prices[it.symbol], live) }
        }.distinctUntilChanged()

    override suspend fun add(instrument: Instrument) {
        // Just persist. The collector above reacts to the new symbol: subscribe + snapshot.
        dao.insert(instrument.toEntity(addedAt = System.currentTimeMillis()))
    }

    override suspend fun remove(symbol: String) {
        dao.delete(symbol)
        priceCache.remove(symbol)
    }

    /** Best-effort REST snapshot to seed price + previousClose; failures are ignored. */
    private suspend fun seedSnapshot(symbol: String) {
        marketDataSource.snapshot(symbol).getOrNull()?.let { quote ->
            priceCache.seed(quote)
            dao.updatePrice(symbol, quote.price, quote.updatedAt)
        }
    }

    private fun WatchedInstrumentEntity.toWatchlistItem(quote: Quote?, live: Boolean): WatchlistItem {
        val price = quote?.price ?: lastPrice
        val updatedAt = quote?.updatedAt ?: lastPriceAt
        val previousClose = quote?.previousClose
        return WatchlistItem(
            instrument = Instrument(symbol = symbol, displayName = displayName, type = type),
            price = price,
            previousClose = previousClose,
            movement = movementOf(price, previousClose),
            // Stale = we have a price but the live feed isn't currently delivering
            // (connecting/reconnecting/offline). A connected-but-quiet symbol is NOT stale.
            isStale = price != null && !live,
            updatedAt = updatedAt,
        )
    }

    private fun movementOf(price: Double?, previousClose: Double?): PriceMovement = when {
        price == null || previousClose == null -> PriceMovement.UNKNOWN
        price > previousClose -> PriceMovement.UP
        price < previousClose -> PriceMovement.DOWN
        else -> PriceMovement.FLAT
    }
}
