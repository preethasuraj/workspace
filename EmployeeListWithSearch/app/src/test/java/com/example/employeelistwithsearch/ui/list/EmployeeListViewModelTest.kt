package com.example.employeelistwithsearch.ui.list

import com.example.employeelistwithsearch.repository.EmployeeRepository
import com.example.employeelistwithsearch.ui.EmployeeUIEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class EmployeeListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
   class FakeRepository(): EmployeeRepository{
       val resultSuccess = Result.failure<List<EmployeeUIEntity>>(Exception("Empty"))
       override suspend fun getEmployees(): Result<List<EmployeeUIEntity>> {
           return resultSuccess
       }

   }

    val viewModel = EmployeeListViewModel(FakeRepository())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `view model state is loading initially` () = runTest {
        val viewModel = EmployeeListViewModel(FakeRepository())
        backgroundScope.launch (UnconfinedTestDispatcher(testScheduler)){
            viewModel.uiState.collect {  }
//
        }
        assertEquals(UiState.Empty, viewModel.uiState.value)
    }


}

class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
