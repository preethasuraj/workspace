package com.example.test6.ui

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.test6.network.PropertyPrice
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Property")
data class UiEntity(
    @PrimaryKey
    val id: String,
    val name:String,
    val photoUrl: String,
    val rating: String?,
    val price: String?,
)