package com.example.test1.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://s3.amazonaws.com/sq-mobile-interview/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesPropertyService(retrofit: Retrofit): PropertyService{
       return retrofit.create(PropertyService::class.java)
    }
}