package com.example.hotels3.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.hotels3.local.HotelDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Insert(HotelDataEntity::class, onConflict = REPLACE)
    suspend fun insertHotels(list: List<HotelDataEntity>)

    @Query("Select * From  HotelDataEntity WHERE name LIKE '%' || :text || '%'")
    fun getHotels(text: String  = "") : Flow<List<HotelDataEntity>>
    @Query("Select * From  HotelDataEntity ")
    fun getHotels() : Flow<List<HotelDataEntity>>
}