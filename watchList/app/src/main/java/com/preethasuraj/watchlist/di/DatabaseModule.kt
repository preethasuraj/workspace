package com.preethasuraj.watchlist.di

import android.content.Context
import androidx.room.Room
import com.preethasuraj.watchlist.data.local.AppDatabase
import com.preethasuraj.watchlist.data.local.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "watchlist.db"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Provides
    fun provideWatchlistDao(database: AppDatabase): WatchlistDao = database.watchlistDao()
}
