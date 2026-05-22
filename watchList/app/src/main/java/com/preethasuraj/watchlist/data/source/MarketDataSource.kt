package com.preethasuraj.watchlist.data.source

import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.PricePoint
import com.preethasuraj.watchlist.domain.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Abstraction over a source of market data. This is the seam that lets us swap a real
 * Finnhub-backed implementation for a fake/demo one (for offline review and tests).
 *
 * Exposes both the REST surface (search + snapshot) and the live trade stream
 * (price stream, connection state, subscribe/unsubscribe).
 */
interface MarketDataSource {

    /** Searches instruments by free-text query. */
    suspend fun search(query: String): Result<List<Instrument>>

    /**
     * Fetches a one-shot price snapshot for [symbol]. A successful result with `null`
     * means the symbol is valid but has no price data available (e.g. delisted or no
     * trades yet), as opposed to a [Result.failure] which indicates a request error.
     */
    suspend fun snapshot(symbol: String): Result<Quote?>

    /** Hot stream of live trade ticks for the currently subscribed symbols. */
    fun priceStream(): Flow<PricePoint>

    /** Lifecycle of the underlying stream connection. */
    val connectionState: StateFlow<ConnectionState>

    /** Adds [symbol] to the live subscription set (opens the stream if needed). */
    fun subscribe(symbol: String)

    /** Removes [symbol] from the live subscription set. */
    fun unsubscribe(symbol: String)
}
