package com.example.test6.network

import com.example.test6.ui.UiEntity
import com.google.gson.annotations.SerializedName

data class NetworkEntity(
    val id: String,
    val name:String,
    @SerializedName("photo_url")
    val photoUrl: String,
    val rating: Float?,
    val price: PropertyPrice?,
) {
}

data class PropertyPrice(
    val amount: Float,
    val currency: String,
    @SerializedName("display_symbol")
    val displaySymbol: String,

)

data class PropertyResponse(
    val properties: List<NetworkEntity>,
)

fun NetworkEntity.toUi(): UiEntity {
    return UiEntity(
        id = this.id,
        name = this.name,
        photoUrl = this.photoUrl,
        rating = this.rating?.let{"${it}*"},
        price = this.price?.let{"${it.displaySymbol}${it.currency}"},
    )
}
