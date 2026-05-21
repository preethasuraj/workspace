package com.example.test6.repository

import com.example.test6.database.PropertyDao
import com.example.test6.network.PropertyService
import com.example.test6.network.toUi
import com.example.test6.ui.UiEntity
import javax.inject.Inject

data class PropertyRepository @Inject constructor(
    val propertyService: PropertyService,
    val providesDao: PropertyDao,
) {
    val properties = providesDao.getAll()
    suspend fun getProperties(): Result<Unit>{
        return try {
            val result = propertyService.fetchProperties()
            if(result.properties.isNotEmpty()){
                providesDao.insertAll(result.properties.map { it.toUi() })
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getProperty(id: String): Result<UiEntity>{
        return try {
            val result = providesDao.findById(id)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}