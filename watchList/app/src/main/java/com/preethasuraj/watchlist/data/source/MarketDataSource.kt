package com.preethasuraj.watchlist.data.source

import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.Quote

/**
 * Abstraction over a source of market data. This is the seam that lets us swap a real
 * Finnhub-backed implementation for a fake/demo one (for offline review and tests).
 *
 * Currently, exposes the REST surface (search + snapshot). The live-streaming members
 * (price stream, connection state, subscribe/unsubscribe) are added in a later phase.
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
}
