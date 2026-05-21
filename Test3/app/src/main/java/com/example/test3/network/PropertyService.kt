package com.example.test3.network

import retrofit2.http.GET

interface PropertyService {

    @GET("hotels")
    suspend fun getProperties(): PropertiesResponse
}