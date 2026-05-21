package com.example.myapplication.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") lim: Int =20
    ): ProductResponse

    @GET("users/{userId}/posts")
    suspend fun getPosts(@Path("userId") id: Int)
}