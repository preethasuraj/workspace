package com.example.test1.ui

sealed class PropertyUiState {
    data object Loading: PropertyUiState()
    data class Error(val message:String): PropertyUiState()
    data object Empty: PropertyUiState()
    data class Success(val properties:List<PropertyUiEntity>): PropertyUiState()
}