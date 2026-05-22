package com.preethasuraj.watchlist.data.source

import com.preethasuraj.watchlist.data.remote.safeApiCall
import com.preethasuraj.watchlist.data.remote.rest.FinnhubApi
import com.preethasuraj.watchlist.data.remote.rest.toInstrumentOrNull
import com.preethasuraj.watchlist.data.remote.rest.toQuoteOrNull
import com.preethasuraj.watchlist.data.remote.ws.WebSocketManager
import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.PricePoint
import com.preethasuraj.watchlist.domain.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Finnhub-backed [MarketDataSource]: REST for search/snapshot (Retrofit suspend functions
 * are main-safe, so no dispatcher hop), and the live trade stream delegated to
 * [WebSocketManager].
 */
@Singleton
class FinnhubMarketDataSource @Inject constructor(
    private val api: FinnhubApi,
    private val webSocketManager: WebSocketManager,
) : MarketDataSource {

    override suspend fun search(query: String): Result<List<Instrument>> =
        safeApiCall { api.search(query) }
            .map { response ->
                response.result
                    .mapNotNull { it.toInstrumentOrNull() }
                    // Finnhub can return several rows per symbol (multiple exchanges,
                    // share classes). Symbol is the streaming/watchlist identity, so we
                    // collapse to one row per symbol (keeps Compose list keys unique too).
                    .distinctBy { it.symbol }
            }

    override suspend fun snapshot(symbol: String): Result<Quote?> =
        safeApiCall { api.quote(symbol) }
            .map { it.toQuoteOrNull(symbol) }

    override fun priceStream(): Flow<PricePoint> = webSocketManager.trades

    override val connectionState: StateFlow<ConnectionState> = webSocketManager.connectionState

    override fun subscribe(symbol: String) = webSocketManager.subscribe(symbol)

    override fun unsubscribe(symbol: String) = webSocketManager.unsubscribe(symbol)
}
