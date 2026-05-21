package com.example.test7.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Insert(DatabaseEntity::class, onConflict = REPLACE)
    suspend fun insertHotels(hotels: List<DatabaseEntity>)

    @Query("SELECT * from Hotel")
    fun getHotels(): Flow<List<DatabaseEntity>>

    @Query("SELECT * from Hotel where Hotel.id LIKE :id")
    suspend fun getHotel(id: String): DatabaseEntity
}