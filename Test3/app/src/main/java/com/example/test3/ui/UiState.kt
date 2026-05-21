package com.example.test3.ui

sealed class UiState {
    data object Loading : UiState()
    data object Empty : UiState()
    data object Error : UiState()
    data class Success(val properties: List<PropertyUiEntity>) : UiState()
}