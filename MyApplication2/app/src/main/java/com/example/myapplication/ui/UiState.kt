package com.example.myapplication.ui

import com.example.myapplication.localsource.Hotel

sealed class UiState {
    data object Loading : UiState()
    data class Success(
        val hotels: List<HotelUiEntity>,
        val isRefreshing: Boolean,
    ) : UiState()
}