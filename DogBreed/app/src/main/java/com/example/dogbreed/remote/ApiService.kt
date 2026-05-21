package com.example.dogbreed.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("breeds/list/all")
    suspend fun getDogBreeds(): RemoteEntity

    @GET("breed/{breed}/images/random/3")
    suspend fun getSubbreedDetails(@Path("breed") breed: String): BreedDetails
}