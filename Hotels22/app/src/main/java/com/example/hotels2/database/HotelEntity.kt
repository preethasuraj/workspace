package com.example.hotels2.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity(tableName = "Hotel")
data class HotelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    val starRating: Int,
    val images: List<String>
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