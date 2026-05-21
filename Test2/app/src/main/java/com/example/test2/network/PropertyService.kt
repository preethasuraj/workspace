package com.example.test2.network

import retrofit2.http.GET

interface PropertyService {
    @GET("employees.json")
    suspend fun getProperties(): PropertyResponse
}