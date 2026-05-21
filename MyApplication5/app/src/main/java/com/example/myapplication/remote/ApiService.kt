package com.example.myapplication.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("carts")
    suspend fun getCart(
        @Query("select") select: String = "id",
        @Query("limit") limit: Int = 10
    ): CartResponse

    @GET("carts/{id}")
    suspend fun getCartDetails(@Path("id") id: Int): CartDetails
}