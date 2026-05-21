package com.example.hitels5.repository

import com.example.hitels5.database.HotelDao
import com.example.hitels5.database.LocalEntity
import com.example.hitels5.remote.RemoteApi
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class HotelRepository @Inject constructor(
    val remoteApi: RemoteApi,
    val dao: HotelDao,
) {

    val hotels = dao.getHotels().map { item ->
        item.map {
            DomainEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                city = it.city,
                images = it.images,
                starRating = it.starRating
            )
        }
    }

    suspend fun getHotels(): Result<Unit> {
        return try {
            dao.addHotels(
                remoteApi.getHotels().hotels
                    .map {
                        LocalEntity(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            city = it.city,
                            images = it.images,
                            starRating = it.starRating
                        )
                    })
            Result.success(Unit)
        } catch (e: Exception) {
            // cancell

            Result.failure(e)
        }
    }


    suspend fun getHotel(id: String?): Result<DomainEntity> {
        return try {
            return Result.success(dao.getHotel(id).run {
                    DomainEntity(
                        id = this.id,
                        name = this.name,
                        description = this.description,
                        city = this.city,
                        images = this.images,
                        starRating = this.starRating
                    )
                })
        } catch (e: Exception) {
            // cancell

            Result.failure(e)
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
