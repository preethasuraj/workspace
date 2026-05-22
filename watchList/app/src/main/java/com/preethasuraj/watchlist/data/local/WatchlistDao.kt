package com.preethasuraj.watchlist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    /** Newest additions first. Emits a new list whenever the table changes. */
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<WatchedInstrumentEntity>>

    /** Reactive membership check, used to toggle add/remove affordances in the UI. */
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE symbol = :symbol)")
    fun isWatched(symbol: String): Flow<Boolean>

    /** Reactive symbol set, used to keep the live price subscriptions in sync. */
    @Query("SELECT symbol FROM watchlist")
    fun observeSymbols(): Flow<List<String>>

    /** Ignores conflicts so re-adding an existing symbol preserves its original addedAt. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: WatchedInstrumentEntity)

    @Query("UPDATE watchlist SET lastPrice = :price, lastPriceAt = :updatedAt WHERE symbol = :symbol")
    suspend fun updatePrice(symbol: String, price: Double, updatedAt: Long)

    @Query("DELETE FROM watchlist WHERE symbol = :symbol")
    suspend fun delete(symbol: String)
}
