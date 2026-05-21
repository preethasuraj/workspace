package com.example.test7.network

import retrofit2.http.GET

interface PropertyService {
    @GET("hotels")
    suspend fun fetchProperties(): NetworkResponse
}