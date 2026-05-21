package com.example.test4.network

import retrofit2.http.GET

interface PropertyService {
    @GET("hotels")
    suspend fun getProperties(): PropertyResponse
}