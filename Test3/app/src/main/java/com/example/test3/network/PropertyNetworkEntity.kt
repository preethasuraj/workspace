package com.example.test3.network

import com.example.test3.ui.PropertyUiEntity
import com.google.gson.annotations.SerializedName

data class PropertyNetworkEntity(
    val id: String,
    val name: String,
    @SerializedName("photo_url")
    val photoUrl: String?,
    val rating: Float?,
    @SerializedName("price")
    val price: PropertyPrice?
)

data class PropertyPrice(
    val amount: Float,
    val currency: String,
    @SerializedName("display_symbol")
    val symbol: String,
) {
    fun format(): String? {
        return "${symbol}${amount}"
    }
}

data class PropertiesResponse(
    val properties: List<PropertyNetworkEntity>
)

fun PropertyNetworkEntity.toUiEntity(): PropertyUiEntity {
    return PropertyUiEntity(
        id = this.id,
        name = this.name,
        photoUrl = this.photoUrl,
        rating = this.rating,
        formattedPrice = this.price?.format()
    )
}