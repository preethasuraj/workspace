package com.example.myapplication.ui

sealed class UiState {
    data object Loading: UiState()
    data class ShowingOrders(val orders: List<UiItem>): UiState()
}