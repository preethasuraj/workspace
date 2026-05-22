package com.preethasuraj.watchlist.presentation.search

import app.cash.turbine.test
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.testutil.FakeWatchlistRepository
import com.preethasuraj.watchlist.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeWatchlistRepository()

    private val aapl = Instrument(symbol = "AAPL", displayName = "Apple Inc", type = "Common Stock")

    private fun viewModel() = SearchViewModel(repository)

    @Test
    fun `initial state is idle`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = viewModel()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals("", initial.query)
            assertTrue(initial.results.isEmpty())
            assertFalse(initial.isSearching)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `a query goes through loading to results after the debounce`() = runTest(mainDispatcherRule.dispatcher) {
        repository.searchResult = Result.success(listOf(aapl))
        // A small latency so the Loading state is observable (otherwise it conflates into Success).
        repository.searchDelayMs = 100
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(SearchUiState(), awaitItem()) // initial

            viewModel.onQueryChange("AAPL")
            // The debounce window must pass before any search runs.
            advanceTimeBy(300)

            val loading = awaitItem()
            assertEquals("AAPL", loading.query)
            assertTrue(loading.isSearching)

            // Let the search latency elapse.
            advanceUntilIdle()

            val success = awaitItem()
            assertFalse(success.isSearching)
            assertEquals(1, success.results.size)
            assertEquals(aapl, success.results.single().instrument)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `results are marked watched based on the repository`() = runTest(mainDispatcherRule.dispatcher) {
        repository.searchResult = Result.success(listOf(aapl))
        repository.watchedSymbols.value = setOf("AAPL")
        val viewModel = viewModel()

        viewModel.uiState.test {
            viewModel.onQueryChange("AAPL")
            advanceTimeBy(300)
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(1, state.results.size)
            assertTrue(state.results.single().isWatched)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `a failed search surfaces an error message`() = runTest(mainDispatcherRule.dispatcher) {
        repository.searchResult = Result.failure(IOException("network down"))
        val viewModel = viewModel()

        viewModel.uiState.test {
            viewModel.onQueryChange("AAPL")
            advanceTimeBy(300)
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals("network down", state.error)
            assertFalse(state.isSearching)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry re-runs the search`() = runTest(mainDispatcherRule.dispatcher) {
        repository.searchResult = Result.failure(IOException("network down"))
        val viewModel = viewModel()

        viewModel.uiState.test {
            viewModel.onQueryChange("AAPL")
            advanceTimeBy(300)
            advanceUntilIdle()
            assertEquals("network down", expectMostRecentItem().error)

            // Recover, then retry without changing the query.
            repository.searchResult = Result.success(listOf(aapl))
            viewModel.onRetry()
            advanceUntilIdle()

            val recovered = expectMostRecentItem()
            assertEquals(null, recovered.error)
            assertEquals(1, recovered.results.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `rapid typing only searches the final query`() = runTest(mainDispatcherRule.dispatcher) {
        repository.searchResult = Result.success(listOf(aapl))
        val viewModel = viewModel()

        viewModel.uiState.test {
            viewModel.onQueryChange("A")
            advanceTimeBy(100) // less than the 300ms debounce
            viewModel.onQueryChange("AA")
            advanceTimeBy(100)
            viewModel.onQueryChange("AAPL")
            advanceTimeBy(300)
            advanceUntilIdle()

            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(listOf("AAPL"), repository.searchQueries)
    }

    @Test
    fun `blank query stays idle without searching`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = viewModel()

        viewModel.uiState.test {
            viewModel.onQueryChange("   ")
            advanceTimeBy(300)
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }

        assertTrue(repository.searchQueries.isEmpty())
    }

    @Test
    fun `add delegates to the repository`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = viewModel()

        viewModel.add(aapl)
        advanceUntilIdle()

        assertEquals(listOf(aapl), repository.added)
    }

    @Test
    fun `remove delegates to the repository`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = viewModel()

        viewModel.remove("AAPL")
        advanceUntilIdle()

        assertEquals(listOf("AAPL"), repository.removed)
    }
}
