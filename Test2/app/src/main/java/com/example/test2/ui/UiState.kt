package com.example.test2.ui

sealed class UiState {
    data object Loading: UiState()
    data object Empty: UiState()
    data object Error: UiState()
    data class Success(val properties: List<PropertyUiEntity>): UiState()
}

sealed class DataError: Exception() {
    data object Empty: DataError()
    data object NetworkError: DataError()
}