package com.example.test5.ui.theme

import com.example.test5.network.PropertyPrice
import com.google.gson.annotations.SerializedName

data class UiEntity(
    val id: String,
    val name: String,
    val photoUrl: String,
    val rating: String?,
    val price: String?
)