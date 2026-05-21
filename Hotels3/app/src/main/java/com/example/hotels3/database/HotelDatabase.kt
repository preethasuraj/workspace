package com.example.hotels3.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hotels3.local.Converters
import com.example.hotels3.local.HotelDataEntity
import com.example.hotels3.ui.HotelUiEntity

@Database(entities = [HotelDataEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class HotelDatabase: RoomDatabase() {
    abstract fun getDao(): HotelDao
}