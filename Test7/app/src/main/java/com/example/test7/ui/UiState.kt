package com.example.test7.ui

import com.example.test7.network.PropertyPrice
import com.google.gson.annotations.SerializedName

sealed class UiState {
    data object Loading: UiState()
    data class Success(val properties: List<UiEntity>): UiState()
}

data class UiEntity(
    val id: String,
    val name: String,
    val photUrl: String,
    val rating: String?,
    val price: String?,
    val type: PropertyType,
)