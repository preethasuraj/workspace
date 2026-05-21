package com.example.hotel6.repository

import android.content.Context
import com.example.hotel6.local.LocalResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class HotelRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    val gson = Gson()
    val json = context.assets.open("Hotels.json")
        .bufferedReader()
        .use { it.readText() }

    fun getHotels(): Result<List<DomainEntity>> {
        return try {
            Result.success(
                gson.fromJson(json, LocalResponse::class.java).hotels
                    .map {
                        DomainEntity(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            starRating = it.starRating,
                            images = it.images
                        )
                    }
            )
        } catch (e: JsonSyntaxException) {
            Result.failure(e)
        }
    }
}

data class DomainEntity(
    val id: String,
    val name: String,
    val description: String,
    val starRating: Int,
    val images: List<String>,
)