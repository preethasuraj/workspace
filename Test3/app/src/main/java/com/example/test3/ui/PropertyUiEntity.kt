package com.example.test3.ui

import com.example.test3.network.PropertyNetworkEntity
import com.example.test3.network.PropertyPrice
import com.google.gson.annotations.SerializedName

data class PropertyUiEntity(
    val id: String,
    val name: String,
    val photoUrl: String?,
    val rating: Float?,
    val formattedPrice: String?
)

