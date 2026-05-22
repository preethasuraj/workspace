package com.preethasuraj.watchlist.presentation.watchlist

import app.cash.turbine.test
import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.PriceMovement
import com.preethasuraj.watchlist.domain.model.WatchlistItem
import com.preethasuraj.watchlist.testutil.FakeWatchlistRepository
import com.preethasuraj.watchlist.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeWatchlistRepository()

    private val item = WatchlistItem(
        instrument = Instrument(symbol = "AAPL", displayName = "Apple Inc", type = "Common Stock"),
        price = 195.0,
        previousClose = 199.0,
        movement = PriceMovement.DOWN,
        isStale = false,
        updatedAt = 1_000L,
    )

    @Test
    fun `initial state is loading`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = WatchlistViewModel(repository)

        viewModel.uiState.test {
            assertTrue(awaitItem().isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits items and connection from the repository`() = runTest(mainDispatcherRule.dispatcher) {
        repository.watchlist.value = listOf(item)
        repository.connection.value = ConnectionState.Connected
        val viewModel = WatchlistViewModel(repository)

        viewModel.uiState.test {
            advanceUntilIdle()
            val state = expectMostRecentItem()
            assertFalse(state.isLoading)
            assertEquals(listOf(item), state.items)
            assertEquals(ConnectionState.Connected, state.connection)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `reflects a connection change`() = runTest(mainDispatcherRule.dispatcher) {
        repository.watchlist.value = listOf(item)
        val viewModel = WatchlistViewModel(repository)

        viewModel.uiState.test {
            advanceUntilIdle()
            assertEquals(ConnectionState.Connected, expectMostRecentItem().connection)

            repository.connection.value = ConnectionState.Reconnecting(attempt = 2)
            advanceUntilIdle()

            assertEquals(ConnectionState.Reconnecting(attempt = 2), expectMostRecentItem().connection)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `remove delegates to the repository`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = WatchlistViewModel(repository)

        viewModel.remove("AAPL")
        advanceUntilIdle()

        assertEquals(listOf("AAPL"), repository.removed)
    }
}
