package com.preethasuraj.watchlist.di

import com.preethasuraj.watchlist.BuildConfig
import com.preethasuraj.watchlist.data.source.FakeMarketDataSource
import com.preethasuraj.watchlist.data.source.FinnhubMarketDataSource
import com.preethasuraj.watchlist.data.source.MarketDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Selects the live Finnhub data source or the offline fake one. Demo mode is used when
 * `USE_FAKE_DATA` is set (`-PuseFakeData=true`) or when no API key is configured — so the
 * app runs out of the box without a key. Providers ensure only the chosen one is created.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMarketDataSource(
        real: Provider<FinnhubMarketDataSource>,
        fake: Provider<FakeMarketDataSource>,
    ): MarketDataSource =
        if (BuildConfig.USE_FAKE_DATA || BuildConfig.FINNHUB_API_KEY.isBlank()) {
            fake.get()
        } else {
            real.get()
        }
}
