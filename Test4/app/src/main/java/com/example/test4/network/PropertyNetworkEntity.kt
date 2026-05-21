package com.example.test4.network

import android.R.attr.rating
import com.example.test4.ui.PropertyUiEntity
import com.google.gson.annotations.SerializedName

data class PropertyNetworkEntity(
    val id: String,
    val name: String,
    @SerializedName("photo_url")
    val photUrl: String,
    val rating: Float?,
    val price: PropertyPrice?,
)

data class PropertyPrice(
    val amount: String,
    val currency: String,
    @SerializedName("display_symbol")
    val displaySymbol: String
)

data class PropertyResponse(
val properties: List<PropertyNetworkEntity>
)

fun PropertyNetworkEntity.toUiEntity(): PropertyUiEntity {
    return PropertyUiEntity(
        id = this.id,
        name = this.name,
        photUrl = this.photUrl,
        rating = this.rating.toUiRating(),
        price = this.price.toUiPrice(),

    )
}

private fun Float?.toUiRating() = this?.let { "${this}*" }

private fun PropertyPrice?.toUiPrice() = this?.let{"${this.displaySymbol}${this.amount}"}
