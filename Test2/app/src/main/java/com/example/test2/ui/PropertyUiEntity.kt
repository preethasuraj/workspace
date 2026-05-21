package com.example.test2.ui

import com.example.test2.network.PropertyNetworkEntity

data class PropertyUiEntity(
    val uuid: String,
    val name: String,
    val smallUrl: String
)

fun PropertyUiEntity.toUiEntity(): PropertyNetworkEntity {
    return PropertyNetworkEntity(
        uuid = this.uuid,
        name = this.name,
        smallUrl = this.smallUrl
    )
}