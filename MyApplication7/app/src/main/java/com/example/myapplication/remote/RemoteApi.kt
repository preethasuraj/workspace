package com.example.myapplication.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApi {

    @GET("users")
    suspend fun getUsers(@Query("limit") limit: Int = 10): UserResponse
}