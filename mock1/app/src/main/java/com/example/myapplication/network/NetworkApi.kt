package com.example.myapplication.network

import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {
    @GET("orders")
    suspend fun getOrders(): OrdersResponse

    @GET("order/{id}")
    suspend fun getOrder(@Path("id") id: Int): OrderResponse

}