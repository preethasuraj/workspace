package com.example.hotel6.local

import com.google.gson.annotations.SerializedName

data class LocalEntity (
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("star_rating")
    val starRating: Int,
    val images: List<String>,
)

data class LocalResponse(
    val hotels: List<LocalEntity>
)