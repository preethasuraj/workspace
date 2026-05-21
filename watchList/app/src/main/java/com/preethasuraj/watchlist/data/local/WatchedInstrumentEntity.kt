package com.preethasuraj.watchlist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A persisted watchlist row. The symbol is the natural primary key (one entry per
 * instrument). [lastPrice]/[lastPriceAt] cache the most recent known price so a cold
 * start can show something immediately (marked stale) before fresh data arrives —
 * this also backs the offline-display behavior.
 */
@Entity(tableName = "watchlist")
data class WatchedInstrumentEntity(
    @PrimaryKey val symbol: String,
    val displayName: String,
    val type: String,
    val addedAt: Long,
    val lastPrice: Double?,
    val lastPriceAt: Long?,
)
