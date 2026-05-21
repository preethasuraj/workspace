package com.example.practice2.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.practice2.network.EmployeeType

@Entity(tableName = "Employee")
data class EmployeeEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val imageUrl: String,
    val imageUrlLarge: String,
    val type: String,
)