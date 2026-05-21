package com.example.employeelistwithsearch.repository

import com.example.employeelistwithsearch.ui.EmployeeUIEntity
import com.example.employeelistwithsearch.ui.list.UiState

interface EmployeeRepository {

    suspend fun getEmployees(): Result<List<EmployeeUIEntity>>
}