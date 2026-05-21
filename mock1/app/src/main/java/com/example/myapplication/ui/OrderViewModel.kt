package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    val repository: OrderRepository
): ViewModel() {
    var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                repository.getOrders()
                    .onFailure {  }
                    .onSuccess {
                        _uiState.value = UiState.ShowingOrders(it)
                    }

            } catch (e: Exception) {
                throw e
            }
        }
    }
}