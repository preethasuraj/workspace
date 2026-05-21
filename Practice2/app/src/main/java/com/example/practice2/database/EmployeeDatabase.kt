package com.example.practice2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.practice2.network.Employee

@Database(entities = [EmployeeEntity::class], version = 3)
abstract class EmployeeDatabase: RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}