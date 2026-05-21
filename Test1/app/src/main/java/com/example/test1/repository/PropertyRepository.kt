package com.example.test1.repository

import com.example.test1.network.PropertyService
import com.example.test1.network.toUiEntity
import com.example.test1.ui.PropertyUiEntity
import javax.inject.Inject


data class PropertyRepository @Inject constructor(
    val propertyService: PropertyService
) {
    suspend fun getProperties(): Result<List<PropertyUiEntity>>{
       return  try {
            val result = propertyService.getProperties()
           if(result.propertyList.isEmpty()){
               Result.failure(Exception("Empty"))
           } else {
                Result.success(
                   result.propertyList.map { it.toUiEntity() }
               )
           }
        }catch(e: Exception){
            Result.failure(e)
        }
    }
}