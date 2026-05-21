package com.example.hotels3.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName

@Entity
data class HotelDataEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val city: String,
    val country: String,
    @SerializedName("star_rating")
    val startRating: Int,
    val images: List<String>,

)

data class HotelResponse(
    val hotels: List<HotelDataEntity>
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