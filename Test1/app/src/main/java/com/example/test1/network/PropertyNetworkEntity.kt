package com.example.test1.network

import com.example.test1.ui.PropertyUiEntity
import com.google.gson.annotations.SerializedName

data class PropertyNetworkEntity(
    val uuid: String,
    @SerializedName("full_name")
    val name: String,
    @SerializedName("photo_url_small")
    val smallUrl: String,
)

data class PropertyResponse(
    @SerializedName("employees")
    val propertyList: List<PropertyNetworkEntity>
)


fun PropertyNetworkEntity.toUiEntity(): PropertyUiEntity {
    return PropertyUiEntity(
        uuid = this.uuid,
        name = this.name,
        smallUrl = this.smallUrl
    )
}