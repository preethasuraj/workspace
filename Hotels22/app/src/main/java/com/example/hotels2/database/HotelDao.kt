package com.example.hotels2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Insert(entity = HotelEntity::class, onConflict = REPLACE)
    suspend fun addHotels(hotels:List<HotelEntity>)

    @Query("SELECT * FROM Hotel")
    fun getHotels(): Flow<List<HotelEntity>>
}