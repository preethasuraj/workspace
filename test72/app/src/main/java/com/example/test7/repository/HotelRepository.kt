package com.example.test7.repository

import android.content.Context
import com.example.test7.database.DatabaseEntity
import com.example.test7.database.HotelDao
import com.example.test7.local.HotelResponse
import com.example.test7.local.toDomainEntity
import com.example.test7.network.NetworkApi
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.CancellationException
import javax.inject.Inject

class HotelRepository @Inject constructor(
    val networkApi: NetworkApi,
    val dao: HotelDao,
) {

    val hotels = dao.getHotels()
        .map { list ->
            list.map {
                DomainEntity(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    description = it.description,
                    starRating = it.starRating,
                    images = it.images
                )
            }
        }


    suspend fun getHotelsRemote(): Result<Unit> {
        try {
            val result = networkApi.getHotels()
            dao.insertHotels(result.hotels.map {
                DatabaseEntity(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    description = it.description,
                    starRating = it.starRating,
                    images = it.images
                )
            })
            return Result.success(
                Unit
            )
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            } else {
                return Result.failure(e)
            }
        }
    }

    suspend fun getHotelDetailsRemote(id: String?): Result<DomainEntity> {
        try {
            return Result.success(hotels.map { hotels ->
                hotels.first { it.id == id }
            }.first())
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            } else {
                return Result.failure(e)
            }
        }
    }


}