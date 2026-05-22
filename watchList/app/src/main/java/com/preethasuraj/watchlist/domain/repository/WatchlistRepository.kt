package com.preethasuraj.watchlist.domain.repository

import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.WatchlistItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Single point of access to the user's watchlist: persistence, the live price merge, and
 * the stream connection state. The presentation layer depends only on this interface.
 */
interface WatchlistRepository {

    /** Searches instruments by free-text query (delegates to the market data source). */
    suspend fun search(query: String): Result<List<Instrument>>

    /** The watchlist merged with live/cached prices, staleness, and movement; newest first. */
    fun observeWatchlist(): Flow<List<WatchlistItem>>

    /** Just the set of watched symbols (lightweight; used to mark search results). */
    fun observeWatchedSymbols(): Flow<Set<String>>

    /** Lifecycle of the live price stream. */
    val connectionState: StateFlow<ConnectionState>

    /** Adds an instrument and best-effort fetches an initial price snapshot. */
    suspend fun add(instrument: Instrument)

    /** Removes an instrument from the watchlist. */
    suspend fun remove(symbol: String)
}
