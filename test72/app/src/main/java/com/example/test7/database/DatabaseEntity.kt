package com.example.test7.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Hotel")
data class DatabaseEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    val starRating: Int,
    val images: List<String>,
)