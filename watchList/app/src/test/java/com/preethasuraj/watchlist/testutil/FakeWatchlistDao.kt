package com.preethasuraj.watchlist.testutil

import com.preethasuraj.watchlist.data.local.WatchedInstrumentEntity
import com.preethasuraj.watchlist.data.local.WatchlistDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** In-memory [WatchlistDao] backed by a StateFlow so observers emit the current value. */
class FakeWatchlistDao : WatchlistDao {

    private val rows = MutableStateFlow<List<WatchedInstrumentEntity>>(emptyList())

    override fun observeAll(): Flow<List<WatchedInstrumentEntity>> =
        rows.map { list -> list.sortedByDescending { it.addedAt } }

    override fun isWatched(symbol: String): Flow<Boolean> =
        rows.map { list -> list.any { it.symbol == symbol } }

    override fun observeSymbols(): Flow<List<String>> =
        rows.map { list -> list.map { it.symbol } }

    override suspend fun insert(entity: WatchedInstrumentEntity) {
        // Mirror OnConflictStrategy.IGNORE: keep the existing row if the symbol is present.
        if (rows.value.none { it.symbol == entity.symbol }) {
            rows.value = rows.value + entity
        }
    }

    override suspend fun updatePrice(symbol: String, price: Double, updatedAt: Long) {
        rows.value = rows.value.map {
            if (it.symbol == symbol) it.copy(lastPrice = price, lastPriceAt = updatedAt) else it
        }
    }

    override suspend fun delete(symbol: String) {
        rows.value = rows.value.filterNot { it.symbol == symbol }
    }
}
