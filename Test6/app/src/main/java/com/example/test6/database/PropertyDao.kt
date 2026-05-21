package com.example.test6.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.test6.ui.UiEntity
import com.example.test6.ui.UiState
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM Property")
    fun getAll(): Flow<List<UiEntity>>


    @Query("SELECT * FROM Property WHERE id LIKE :id")
    suspend fun findById(id: String): UiEntity

    @Insert(entity = UiEntity::class, onConflict = REPLACE)
    suspend fun insertAll(properties: List<UiEntity>)

}