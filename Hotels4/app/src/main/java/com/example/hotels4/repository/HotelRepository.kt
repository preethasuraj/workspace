package com.example.hotels4.repository

import com.example.hotels4.remote.RemoteApi
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

class HotelRepository @Inject constructor(
    val remoteApi: RemoteApi,
) {
    suspend fun getHotels(): Result<List<DomainEntity>> {
        try {
            return Result.success(remoteApi.getHotels()
                .hotels.map {
                    DomainEntity(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        city = it.city,
                        images = it.images,
                        starRating = it.starRating
                    )
                }
            )
        } catch (e: Exception) {
           // throw cancellation exception
            return Result.failure(e)
        }
    }
}

data class DomainEntity(
    val id: String,
    val name: String,
    val description: String,
    val city: String,
    val images: List<String>,
    val starRating: Int
)