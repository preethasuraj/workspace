package com.example.dogbreed.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteApi {
    @GET("all")
    suspend fun getCategories() : RemoteEntity

    @GET("{breed}/images/random/3")
    suspend fun getDetails(@Path("breed") breed: String): BreedDetailsRemote
}