package com.example.employeelistwithsearch.repository

import com.example.employeelistwithsearch.network.Employee
import com.example.employeelistwithsearch.network.EmployeeService
import com.example.employeelistwithsearch.network.toEmployeeUIEntity
import com.example.employeelistwithsearch.ui.EmployeeUIEntity
import com.example.employeelistwithsearch.ui.list.UiState
import javax.inject.Inject


class RealEmployeeRepository @Inject constructor(
    val employeeService: EmployeeService
): EmployeeRepository {
    override suspend fun getEmployees(): Result<List<EmployeeUIEntity>> {

        employeeService.getEmployeeResponse()
        try {
            val result = employeeService.getEmployeeResponse()
            return if(result.employees.isEmpty()){
                Result.failure(Exception("Empty"))
            } else {
                Result.success(

                        result.employees
                            .map { it.toEmployeeUIEntity() })
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}