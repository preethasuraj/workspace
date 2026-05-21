package com.example.test4.repository

import com.example.test4.network.PropertyService
import com.example.test4.network.toUiEntity
import com.example.test4.ui.PropertyUiEntity
import javax.inject.Inject


//todo have it as a interface with Real and fake impl
data class PropertyRepository @Inject constructor(
val propertyService: PropertyService,
) {
    suspend fun getProperties(): Result<List<PropertyUiEntity>> {
       return try {
            val result = propertyService.getProperties()
            if(result.properties.isEmpty()){
                Result.failure(NetworkError.EmptyResponse)
            } else {
                Result.success(result.properties.map{
                    it.toUiEntity()
                })
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.FetchError)
        }

    }
}