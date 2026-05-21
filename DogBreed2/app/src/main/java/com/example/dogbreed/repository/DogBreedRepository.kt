package com.example.dogbreed.repository

import com.example.dogbreed.remote.RemoteApi
import javax.inject.Inject

class DogBreedRepository @Inject constructor(
    private val remoteApi: RemoteApi,
) {
    suspend fun getDogBreed(): Result<Map<String, List<String>>> {
        return try {
            Result.success(remoteApi.getCategories().message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}