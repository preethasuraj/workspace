package com.example.hotels2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HotelEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract  class HotelDatabase: RoomDatabase() {
    abstract fun hotelDao(): HotelDao
}