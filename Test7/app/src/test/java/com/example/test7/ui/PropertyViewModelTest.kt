package com.example.test7.ui

import com.example.test7.network.NetworkEntity
import com.example.test7.network.NetworkResponse
import com.example.test7.network.PropertyService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.collections.listOf

class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class PropertyViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    val propertyService = mockk<PropertyService>()
    val viewModel = PropertyViewModel(
        propertyService
    )

    @Test
    fun initialStateIsLoading() = runTest {
        assertEquals(viewModel.uiState.value, UiState.Loading)

    }

    @Test
    fun stateIsUpdatedOnFetchingData() = runTest {
        coEvery {
            propertyService.fetchProperties()
        }.returns((NetworkResponse(emptyList())))
        val viewModel = PropertyViewModel(
            propertyService
        )
        assertEquals(viewModel.uiState.value, UiState.Loading)

    }

}