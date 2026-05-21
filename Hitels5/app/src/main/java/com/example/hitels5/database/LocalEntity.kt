package com.example.hitels5.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter


@Entity(tableName = "Hotel")
data class LocalEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val city: String,
    val images: List<String>,
    val starRating: Int
)

class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",")
    }
}