package com.example.hitels5.remote

import com.google.gson.annotations.SerializedName

data class RemoteEntity(
    val id: String,
    val name: String,
    val description: String,
    val city: String,
    val images: List<String>,
    @SerializedName("star_rating")
    val starRating: Int
)

data class HotelResponse(
    val hotels: List<RemoteEntity>
)