package com.example.test5.network.repository

import com.example.test5.network.PropertyService
import com.example.test5.network.toUi
import com.example.test5.ui.PropertyUiEntity
import javax.inject.Inject

data class PropertyRepository @Inject constructor(
    val propertyService: PropertyService,
) {
    suspend fun getProperties(): Result<List<PropertyUiEntity>> {
        return try {
            val result = propertyService.getProperties()
            if (result.properties.isEmpty()) {
                Result.failure(NetworkError.Empty)
            } else {
                Result.success(result.properties.map {
                    it.toUi()
                })
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.Error)
        }

    }
}

sealed class NetworkError : Exception() {
    data object Empty : NetworkError()
    data object Error : NetworkError()
}