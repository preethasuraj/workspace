package com.example.paging3.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ItemService {

    @GET("items")
    suspend fun getItems(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): ItemResponse
}