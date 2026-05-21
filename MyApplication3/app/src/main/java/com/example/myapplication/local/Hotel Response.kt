package com.example.myapplication.local

import com.google.gson.annotations.SerializedName

data class HotelResponse(
    val hotels: List<Hotel>
)

data class Hotel(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    @SerializedName("star-rating")
    val starRating: Int,
    val images: List<String>,
)