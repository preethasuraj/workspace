package com.preethasuraj.watchlist.data.repository

import com.preethasuraj.watchlist.data.local.WatchlistDao
import com.preethasuraj.watchlist.data.local.toDomain
import com.preethasuraj.watchlist.data.local.toEntity
import com.preethasuraj.watchlist.data.source.MarketDataSource
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.WatchedInstrument
import com.preethasuraj.watchlist.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Room (DAO) and Retrofit (via [MarketDataSource]) suspend functions are already
 * main-safe, so no explicit dispatcher hop is needed here; the remaining work (mapping,
 * timestamps) is trivial. Tests drive these methods directly with `runTest` + fakes.
 */
@Singleton
class WatchlistRepositoryImpl @Inject constructor(
    private val dao: WatchlistDao,
    private val marketDataSource: MarketDataSource,
) : WatchlistRepository {

    override suspend fun search(query: String): Result<List<Instrument>> =
        marketDataSource.search(query)

    override fun observeWatchlist(): Flow<List<WatchedInstrument>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun isWatched(symbol: String): Flow<Boolean> = dao.isWatched(symbol)

    override suspend fun add(instrument: Instrument) {
        // Persist first so the entry appears immediately, even with no price yet.
        dao.insert(instrument.toEntity(addedAt = System.currentTimeMillis()))
        // Best-effort snapshot: a failure here must not undo the add.
        marketDataSource.snapshot(instrument.symbol).getOrNull()?.let { quote ->
            dao.updatePrice(instrument.symbol, quote.price, quote.updatedAt)
        }
    }

    override suspend fun remove(symbol: String) {
        dao.delete(symbol)
    }
}
