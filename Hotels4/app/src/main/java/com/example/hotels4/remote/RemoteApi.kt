package com.example.hotels4.remote

import retrofit2.http.GET

interface RemoteApi {
    @GET("aa4e2939-c546-43f8-bd71-32acdee83b9d")
    suspend fun getHotels(): RemoteResponse

    //https://mocki.io/v1/
}