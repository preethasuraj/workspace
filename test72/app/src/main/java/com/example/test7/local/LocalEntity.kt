package com.example.test7.local

import com.example.test7.repository.DomainEntity
import com.google.gson.annotations.SerializedName

data class LocalEntity(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    @SerializedName("star_rating")
    val starRating: Int,
    val images: List<String>,
)


data class HotelResponse(
val hotels: List<LocalEntity>
)

fun LocalEntity.toDomainEntity(): DomainEntity {
    return DomainEntity(
        id = this.id,
        name = this.name,
        city = this.city,
        description = this.description,
        starRating = this.starRating,
        images = this.images
    )
}
