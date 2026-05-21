package com.example.employeelistwithsearch.network

import retrofit2.http.GET

interface EmployeeService {
    @GET("employees.json")
    suspend fun getEmployeeResponse(): EmployeeResponse
}