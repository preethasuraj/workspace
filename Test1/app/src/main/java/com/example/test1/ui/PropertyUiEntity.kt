package com.example.test1.ui

import com.example.test1.network.PropertyNetworkEntity
import com.google.gson.annotations.SerializedName

data class PropertyUiEntity(
    val uuid: String,
    val name: String,
    val smallUrl: String,
)

fun PropertyUiEntity.toNetworkEntity(): PropertyNetworkEntity {
    return PropertyNetworkEntity(
        uuid = this.uuid,
        name = this.name,
        smallUrl = this.smallUrl
    )
}