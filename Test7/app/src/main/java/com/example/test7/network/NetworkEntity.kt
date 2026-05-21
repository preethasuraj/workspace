package com.example.test7.network

import com.example.test7.ui.UiEntity
import com.google.gson.annotations.SerializedName

data class NetworkEntity(
    val id: String,
    val name: String,
    @SerializedName("photo_url")
    val photUrl: String,
    val rating: Float?,
    val price: PropertyPrice?,
) {
}

data class PropertyPrice(
    val amount: Float,
    val currency: String,
    @SerializedName("display_symbol")
    val displaySymbol: String,
    val type: String,
)

data class NetworkResponse(
    val properties: List<NetworkEntity>,
)

fun NetworkEntity.toUi(): UiEntity {
    return UiEntity(
        id = this.id,
        name = this.name,
        photUrl = this.photUrl,
        rating = this.rating?.let { "$it*" },
        price = this.price?.let{"${it.displaySymbol}${it.currency}"}
    )
}