package com.example.dogbreed.repository

import com.example.dogbreed.remote.ApiService
import com.example.dogbreed.remote.RemoteEntity
import javax.inject.Inject

class DogRepository @Inject constructor(
    val apiService: ApiService
) {
private var breedMap = mutableMapOf<String, List<String>>()
    suspend fun getDogBreeds(): Result<DomainListEntity> {
        return try {
            breedMap = apiService.getDogBreeds().message.toMutableMap()
            Result.success(
                DomainListEntity(
                    breedMap
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSubbreedDetails(
        breed: String,
    ): Result<DomainDetailsEntity> {
        return try {
            val result = apiService.getSubbreedDetails(breed)
            Result.success(
                DomainDetailsEntity(
                    breed = breed,
                    subbreed = breedMap[breed] ?: emptyList(),
                    images = result.message
                )
            )

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class DomainListEntity(
    val message: Map<String, List<String>>
)

data class DomainDetailsEntity(
    val breed: String,
    val subbreed: List<String>,
    val images: List<String>
)