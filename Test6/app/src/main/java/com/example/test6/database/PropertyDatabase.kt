package com.example.test6.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test6.ui.UiEntity

@Database(entities = [UiEntity::class], version = 1)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
}