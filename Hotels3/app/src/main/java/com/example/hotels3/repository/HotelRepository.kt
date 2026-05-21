package com.example.hotels3.repository

import android.content.Context
import com.example.hotels3.database.HotelDao
import com.example.hotels3.local.HotelDataEntity
import com.example.hotels3.local.HotelResponse
import com.example.hotels3.ui.HotelUiEntity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HotelRepository @Inject constructor(
    val gson: Gson,
    @ApplicationContext val context: Context,
    val dao: HotelDao,
) {
    private val json = context.assets.open("Hotels.json")
        .bufferedReader()
        .use { it.readText() }
    val hotels = dao.getHotels()

    suspend fun getHotels(): Result<Unit> {
        return try {
            val result = gson.fromJson(
                json, HotelResponse::class.java
            )
            dao.insertHotels(result.hotels)
           Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    fun searchHotels(text: String): Flow<List<HotelDataEntity>> {
        return dao.getHotels(text)
    }

    fun getDetails(id: String?): Result<HotelDataEntity> {
        return try {
            val result = gson.fromJson(
                json, HotelResponse::class.java
            )
            Result.success(
                result.hotels.first{it.id == id}
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}