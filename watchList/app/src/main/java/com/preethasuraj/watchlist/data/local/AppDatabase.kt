package com.preethasuraj.watchlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for the watchlist.
 *
 * `exportSchema = false` keeps the build simple for this exercise; a production app
 * would export the schema and add migrations + migration tests as the schema evolves.
 */
@Database(
    entities = [WatchedInstrumentEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}
