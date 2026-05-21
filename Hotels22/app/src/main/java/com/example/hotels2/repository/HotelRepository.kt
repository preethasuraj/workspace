package com.example.hotels2.repository

import android.content.Context
import com.example.hotels2.database.HotelDao
import com.example.hotels2.database.HotelEntity
import com.example.hotels2.local.Hotel
import com.example.hotels2.local.HotelResponse
import com.example.hotels2.remote.RemoteApi
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HotelRepository @Inject constructor(
    val gson: Gson,
    @ApplicationContext val context: Context,
    val remoteApi: RemoteApi,
    val dao: HotelDao,
) {
    val hotels = dao.getHotels()

    private val json = context.assets.open("Hotels.json")
        .bufferedReader()
        .use { it.readText() }

    fun getHotelsLocal(): Result<List<Hotel>> {
        return Result.success(
            (gson.fromJson(json, HotelResponse::class.java).hotels)
        )
    }

    suspend fun getHotelsRemote(): Result<Unit> {
        try {
            val result = remoteApi.getHotels()
            dao.addHotels(
                result.hotels.map {
                    HotelEntity(
                        id = it.id,
                        name = it.name,
                        city = it.city,
                        description = it.description,
                        starRating = it.starRating,
                        images = it.images
                    )
                }
            )
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }

    }

    fun getDetails(id: String): Result<Hotel> {
        return Result.success(
            gson.fromJson(json, HotelResponse::class.java).hotels
                .first { it.id == id }
        )
    }

    fun getHotels(query: String): Flow<List<HotelEntity>> {
        return hotels.map { it ->
            it.filter { hotel ->
                hotel.name.contains(query, true)
            }
        }
    }
}