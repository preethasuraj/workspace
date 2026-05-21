package com.example.test5.network

import android.media.Rating
import com.example.test5.ui.PropertyUiEntity
import com.google.gson.annotations.SerializedName

data class PropertyNetworkEntity(
    val id: String,
    val name: String,
    @SerializedName("photo_url")
    val url: String,
    val rating: Float?,
    val price: PropertyPrice?,
)

data class PropertyPrice(
    val amount: Float,
    val currency: String,
    @SerializedName("display_symbol")
    val displaySymbol: String
)

data class PropertyNetworkResponse(
    val properties: List<PropertyNetworkEntity>
)

fun PropertyNetworkEntity.toUi() : PropertyUiEntity {
    return PropertyUiEntity(
        id = this.id,
        name = this.name,
        url = this.url,
        rating = this.rating?.let { "${it}*" },
        price = this.price?.let { "${it.displaySymbol}${it.amount}" }
    )
}