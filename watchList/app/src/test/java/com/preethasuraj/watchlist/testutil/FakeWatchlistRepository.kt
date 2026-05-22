package com.preethasuraj.watchlist.testutil

import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.WatchlistItem
import com.preethasuraj.watchlist.domain.repository.WatchlistRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Controllable [WatchlistRepository] for ViewModel tests. */
class FakeWatchlistRepository : WatchlistRepository {

    val watchlist = MutableStateFlow<List<WatchlistItem>>(emptyList())
    val watchedSymbols = MutableStateFlow<Set<String>>(emptySet())
    val connection = MutableStateFlow<ConnectionState>(ConnectionState.Connected)

    var searchResult: Result<List<Instrument>> = Result.success(emptyList())
    var searchDelayMs: Long = 0
    val searchQueries = mutableListOf<String>()
    val added = mutableListOf<Instrument>()
    val removed = mutableListOf<String>()

    override suspend fun search(query: String): Result<List<Instrument>> {
        searchQueries += query
        if (searchDelayMs > 0) delay(searchDelayMs)
        return searchResult
    }

    override fun observeWatchlist(): Flow<List<WatchlistItem>> = watchlist

    override fun observeWatchedSymbols(): Flow<Set<String>> = watchedSymbols

    override val connectionState: StateFlow<ConnectionState> = connection

    override suspend fun add(instrument: Instrument) {
        added += instrument
        watchedSymbols.update { it + instrument.symbol }
    }

    override suspend fun remove(symbol: String) {
        removed += symbol
        watchedSymbols.update { it - symbol }
    }
}
