package com.example.hotels1.local

import com.google.gson.annotations.SerializedName

data class HotelResponse(
    val hotels: List<Hotel>
)

data class Hotel(
    val id: String,
    val name: String,
    val description: String,
    val images: List<String>,
    @SerializedName("star_rating")
    val starRating: Int
)


