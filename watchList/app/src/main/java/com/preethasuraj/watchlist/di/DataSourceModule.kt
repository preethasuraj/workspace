package com.preethasuraj.watchlist.di

import com.preethasuraj.watchlist.data.source.FinnhubMarketDataSource
import com.preethasuraj.watchlist.data.source.MarketDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the [MarketDataSource] interface to its real implementation. When the demo/fake
 * source is introduced, this binding is where the swap happens.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMarketDataSource(impl: FinnhubMarketDataSource): MarketDataSource
}
