package com.example.test6.network

import retrofit2.http.GET

interface PropertyService {
    @GET("hotels")
    suspend fun fetchProperties(): PropertyResponse
}