package com.example.employeelistwithsearch.repository

import com.example.employeelistwithsearch.network.EmployeeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {


    @Provides
    fun employeeRepository(employeeService: EmployeeService): EmployeeRepository {
        return RealEmployeeRepository(employeeService)
    }
}