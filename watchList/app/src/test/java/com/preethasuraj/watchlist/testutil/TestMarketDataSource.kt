package com.preethasuraj.watchlist.testutil

import com.preethasuraj.watchlist.data.source.MarketDataSource
import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.PricePoint
import com.preethasuraj.watchlist.domain.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Controllable [MarketDataSource] test double: settable connection, push-able ticks, and recorded subscriptions. */
class TestMarketDataSource : MarketDataSource {

    val connectionFlow = MutableStateFlow<ConnectionState>(ConnectionState.Connected)
    override val connectionState: StateFlow<ConnectionState> = connectionFlow

    val priceFlow = MutableSharedFlow<PricePoint>(extraBufferCapacity = 64)
    val subscribed = mutableListOf<String>()
    val unsubscribed = mutableListOf<String>()

    var searchResult: Result<List<Instrument>> = Result.success(emptyList())
    var snapshotProvider: (String) -> Result<Quote?> = { Result.success(null) }

    override suspend fun search(query: String): Result<List<Instrument>> = searchResult

    override suspend fun snapshot(symbol: String): Result<Quote?> = snapshotProvider(symbol)

    override fun priceStream(): Flow<PricePoint> = priceFlow

    override fun subscribe(symbol: String) {
        subscribed += symbol
    }

    override fun unsubscribe(symbol: String) {
        unsubscribed += symbol
    }
}
