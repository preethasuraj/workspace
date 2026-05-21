package com.example.test3.repository

import android.util.Log
import com.example.test3.network.PropertyService
import com.example.test3.network.toUiEntity
import com.example.test3.ui.PropertyUiEntity
import javax.inject.Inject

data class PropertyRepository @Inject constructor(
    val propertyService: PropertyService
) {
    suspend fun getProperties(): Result<List<PropertyUiEntity>> {
        return  try {
            val result = propertyService.getProperties()
            if(result.properties.isEmpty()){
                Result.failure(DataError.Empty)
            } else {
                Result.success(result.properties.map { it.toUiEntity() })
            }
        } catch (e: Exception) {
            Log.d("Preetha", "getProperties: ${e.message}")
            Result.failure(DataError.NetworkError)
        }

    }
}