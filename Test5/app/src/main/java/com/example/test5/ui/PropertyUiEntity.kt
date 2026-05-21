package com.example.test5.ui

import com.example.test5.network.PropertyPrice
import com.google.gson.annotations.SerializedName

data class PropertyUiEntity(
    val id: String,
    val name: String,
    val url: String,
    val rating: String?,
    val price: String?,
)