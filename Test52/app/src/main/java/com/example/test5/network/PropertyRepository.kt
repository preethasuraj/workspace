package com.example.test5.network

import com.example.test5.ui.theme.UiEntity
import dagger.Reusable
import javax.inject.Inject

class PropertyRepository @Inject constructor(
    val propertyService: PropertyService,
) {
    suspend fun getProperties(): Result<List<UiEntity>> {
        return try {
           val result = propertyService.getProperties()
            if(result.properties.isEmpty()){
                Result.failure(NetworkError.Empty)
            } else {
                Result.success(
                    result.properties.map { it.toUi() }
                )
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.Error)
        }

    }
}

sealed class NetworkError: Exception() {
    data object Error: NetworkError()
    data object Empty: NetworkError()
}