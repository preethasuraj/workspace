package com.example.test2.repository

import com.example.test2.network.PropertyService
import com.example.test2.network.toUiEntity
import com.example.test2.ui.DataError
import com.example.test2.ui.PropertyUiEntity
import javax.inject.Inject

data class PropertyRepository @Inject constructor(
    val propertyService: PropertyService,
) {

    suspend fun getProperties(): Result<List<PropertyUiEntity>>{
        return try {
            val result = propertyService.getProperties()
            if(result.properties.isEmpty()){
                Result.failure(DataError.Empty)
            } else {
                Result.success(
                    result.properties
                        .map{it.toUiEntity()}
                )
            }
        } catch (e: Exception) {
            Result.failure(DataError.NetworkError)
        }
    }

}