package com.example.employeelistwithsearch.network

import androidx.compose.ui.tooling.preview.Preview
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesEmployeeService(retrofit: Retrofit): EmployeeService {
        return  retrofit.create(EmployeeService::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://s3.amazonaws.com/sq-mobile-interview/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}