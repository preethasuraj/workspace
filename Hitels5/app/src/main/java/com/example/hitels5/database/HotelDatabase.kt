package com.example.hitels5.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LocalEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class HotelDatabase(): RoomDatabase() {
    abstract fun getDao() : HotelDao
}