package com.example.test6.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun providePropertyService(retrofit: Retrofit): PropertyService {
        return retrofit.create(PropertyService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://expedia-mock.free.beeceptor.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}