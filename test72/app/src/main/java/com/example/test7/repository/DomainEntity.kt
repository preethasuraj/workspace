package com.example.test7.repository

import com.example.test7.local.LocalEntity
import com.google.gson.annotations.SerializedName

class DomainEntity(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    @SerializedName("star_rating")
    val starRating: Int,
    val images: List<String>
)


