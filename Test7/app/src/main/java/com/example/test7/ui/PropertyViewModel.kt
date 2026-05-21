package com.example.test7.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test7.network.PropertyService
import com.example.test7.network.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    val propertyService: PropertyService,
): ViewModel() {
    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchProperties()
    }

    private fun fetchProperties() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = propertyService.fetchProperties()
                // handle empty
                _uiState.value = UiState.Success(result.properties.map { it.toUi() })
            } catch (e: Exception) {
                // add error state
                throw e
            }
        }
    }
}