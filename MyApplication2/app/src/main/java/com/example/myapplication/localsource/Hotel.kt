package com.example.myapplication.localsource

import com.google.gson.annotations.SerializedName

data class Hotel(
    val id: String,
    val name: String,
    val city: String,
    val country: String,
    @SerializedName("star_rating") val starRating: Int,
    val description: String,
    val images: List<String>
)

data class HotelResponse(
    val hotels: List<Hotel>
)
