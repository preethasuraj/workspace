package com.example.myapplication.list

import com.example.myapplication.remote.User
import com.example.myapplication.repository.Repository
import com.example.myapplication.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class ListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    class FakeRepository(): Repository {
        var result = Result.success<List<User>>(emptyList())
        override suspend fun getUsers(): Result<List<User>> {
            return result
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `inital state is loading`() = runTest {

        val repository: FakeRepository = FakeRepository()
//        repository.result =
        val viewModel = ListViewModel(repository)
        assertEquals(viewModel.uiState.value, UiState.Loading)
        advanceUntilIdle()
        assertEquals(viewModel.uiState.value, UiState.Loading)

    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        // Redirects Dispatchers.Main to the test dispatcher
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        // Resets Dispatchers.Main to the original dispatcher
        Dispatchers.resetMain()
    }
}