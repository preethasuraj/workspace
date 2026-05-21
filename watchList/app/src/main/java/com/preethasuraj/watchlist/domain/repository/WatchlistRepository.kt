package com.preethasuraj.watchlist.domain.repository

import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.WatchedInstrument
import kotlinx.coroutines.flow.Flow

/**
 * Single point of access to the user's watchlist. The presentation layer depends only
 * on this interface. Live price streaming is added to it in a later phase; for now it
 * covers persistence plus a best-effort initial snapshot on add.
 */
interface WatchlistRepository {

    /** Searches instruments by free-text query (delegates to the market data source). */
    suspend fun search(query: String): Result<List<Instrument>>

    /** Observes the persisted watchlist, newest first. */
    fun observeWatchlist(): Flow<List<WatchedInstrument>>

    /** Reactive membership check for a single symbol. */
    fun isWatched(symbol: String): Flow<Boolean>

    /** Adds an instrument and best-effort fetches an initial price snapshot. */
    suspend fun add(instrument: Instrument)

    /** Removes an instrument from the watchlist. */
    suspend fun remove(symbol: String)
}
