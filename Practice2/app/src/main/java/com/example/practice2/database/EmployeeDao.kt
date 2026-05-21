package com.example.practice2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.practice2.network.Employee
import kotlinx.coroutines.flow.Flow

@Dao()
interface EmployeeDao {
    @Query("Select * from Employee")
    fun getEmployees(): Flow<List<EmployeeEntity>>

    @Query("Select * from Employee where uuid = :uuid")
    suspend fun getEmployee(uuid: String): EmployeeEntity?

    @Insert(entity = EmployeeEntity::class, onConflict = REPLACE)
    suspend fun addEmployees(employees: List<EmployeeEntity>)
}