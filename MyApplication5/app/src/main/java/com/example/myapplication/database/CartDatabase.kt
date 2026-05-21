package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class, Product::class], version = 1)
abstract class CartDatabase(): RoomDatabase() {
    abstract fun cartDao(): CartDao
}