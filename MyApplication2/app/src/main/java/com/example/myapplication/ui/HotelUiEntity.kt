package com.example.myapplication.ui

import com.google.gson.annotations.SerializedName

data class HotelUiEntity(
    val id: String,
    val name: String,
    val city: String,
    val country: String,
    val starRating: Int,
    val description: String,
    val images: List<String>,
    var isExpanded: Boolean = false,
)
