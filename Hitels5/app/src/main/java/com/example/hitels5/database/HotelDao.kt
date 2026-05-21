package com.example.hitels5.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Insert(LocalEntity::class, onConflict = REPLACE)
    suspend fun addHotels(hotels: List<LocalEntity>)

    @Query("Select * from Hotel")
    fun getHotels(): Flow<List<LocalEntity>>

    @Query("Select * from Hotel where id like '%' || :id || '%'")
    suspend  fun getHotel(id: String?): LocalEntity
}