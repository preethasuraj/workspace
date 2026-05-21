package com.example.test7.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DatabaseEntity::class,], version = 1)
@TypeConverters(DatabaseModule.Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun hotelDao(): HotelDao
}