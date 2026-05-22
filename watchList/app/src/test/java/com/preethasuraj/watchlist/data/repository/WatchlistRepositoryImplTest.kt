package com.preethasuraj.watchlist.data.repository

import app.cash.turbine.test
import com.preethasuraj.watchlist.data.local.WatchedInstrumentEntity
import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.PriceMovement
import com.preethasuraj.watchlist.domain.model.PricePoint
import com.preethasuraj.watchlist.domain.model.Quote
import com.preethasuraj.watchlist.testutil.FakeWatchlistDao
import com.preethasuraj.watchlist.testutil.TestMarketDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistRepositoryImplTest {

    private val dao = FakeWatchlistDao()
    private val dataSource = TestMarketDataSource()
    private val cache = PriceCache()

    private val aapl = Instrument(symbol = "AAPL", displayName = "Apple Inc", type = "Common Stock")

    /**
     * Builds the repo with an [UnconfinedTestDispatcher] so its two init collectors
     * (price-stream pump + subscription sync) subscribe eagerly at construction — they're
     * then ready to react synchronously to a saved-symbol change or a pushed tick. The job is
     * tied to `backgroundScope`, so `runTest` cancels the collectors when the test ends.
     */
    private fun TestScope.buildRepository(): WatchlistRepositoryImpl =
        WatchlistRepositoryImpl(
            dao,
            dataSource,
            cache,
            backgroundScope + UnconfinedTestDispatcher(testScheduler),
        )

    private suspend fun seedWatchedAaplWithQuote() {
        dao.insert(
            WatchedInstrumentEntity(
                symbol = "AAPL",
                displayName = "Apple Inc",
                type = "Common Stock",
                addedAt = 1_000L,
                lastPrice = null,
                lastPriceAt = null,
            )
        )
        cache.seed(Quote("AAPL", price = 195.0, previousClose = 199.0, updatedAt = 1_000L))
    }

    @Test
    fun `adding an instrument persists it and subscribes to the stream`() = runTest {
        val repository = buildRepository()

        repository.add(aapl)
        advanceUntilIdle()

        assertTrue(dataSource.subscribed.contains("AAPL"))
        assertEquals(setOf("AAPL"), repository.observeWatchedSymbols().first())
    }

    @Test
    fun `removing an instrument unsubscribes and clears its cached price`() = runTest {
        val repository = buildRepository()
        repository.add(aapl)
        advanceUntilIdle()
        cache.seed(Quote("AAPL", price = 195.0, previousClose = 199.0, updatedAt = 1_000L))

        repository.remove("AAPL")
        advanceUntilIdle()

        assertTrue(dataSource.unsubscribed.contains("AAPL"))
        assertNull(cache.prices.value["AAPL"])
        assertEquals(emptySet<String>(), repository.observeWatchedSymbols().first())
    }

    @Test
    fun `observeWatchlist merges the cached quote and derives a downward movement`() = runTest {
        seedWatchedAaplWithQuote()
        val repository = buildRepository()
        advanceUntilIdle()

        val item = repository.observeWatchlist().first().single()
        assertEquals(195.0, item.price)
        assertEquals(199.0, item.previousClose)
        assertEquals(PriceMovement.DOWN, item.movement) // 195 < 199
        assertFalse(item.isStale) // Connected
    }

    @Test
    fun `a live tick updates the price and preserves the previous close`() = runTest {
        seedWatchedAaplWithQuote()
        val repository = buildRepository()

        repository.observeWatchlist().test {
            // Baseline seeded state.
            assertEquals(195.0, awaitItem().single().price)

            // Tick above the previous close -> UP, previousClose carried over from the cache.
            dataSource.priceFlow.emit(PricePoint("AAPL", price = 201.0, epochMs = 2_000L))
            advanceUntilIdle()

            val ticked = awaitItem().single()
            assertEquals(201.0, ticked.price)
            assertEquals(199.0, ticked.previousClose)
            assertEquals(PriceMovement.UP, ticked.movement)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `a priced item becomes stale when the connection is not connected`() = runTest {
        seedWatchedAaplWithQuote()
        val repository = buildRepository()

        repository.observeWatchlist().test {
            assertFalse(awaitItem().single().isStale) // Connected

            dataSource.connectionFlow.value = ConnectionState.Reconnecting(attempt = 1)
            advanceUntilIdle()

            assertTrue(awaitItem().single().isStale)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `connectionState is delegated to the data source`() = runTest {
        val repository = buildRepository()
        dataSource.connectionFlow.value = ConnectionState.Connecting

        assertEquals(ConnectionState.Connecting, repository.connectionState.value)
    }

    @Test
    fun `search delegates to the data source`() = runTest {
        dataSource.searchResult = Result.success(listOf(aapl))
        val repository = buildRepository()

        assertEquals(listOf(aapl), repository.search("apple").getOrNull())
    }
}
