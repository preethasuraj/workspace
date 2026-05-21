package com.example.myapplication.List

import com.example.myapplication.local.Hotel
import com.google.gson.annotations.SerializedName

sealed class ListUiState {

    data object Loading: ListUiState()
    data class Success(
        val list: List<HotelUiEntity>
    ): ListUiState()
}

data class HotelUiEntity(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    val starRating: String,
    val images: List<String>,
)