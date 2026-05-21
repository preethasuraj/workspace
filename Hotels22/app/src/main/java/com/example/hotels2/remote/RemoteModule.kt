package com.example.hotels2.remote

import com.example.hotels2.local.HotelResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mocki.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteApi(retrofit: Retrofit): RemoteApi {
        return retrofit.create(RemoteApi::class.java)
    }


}

interface RemoteApi{
    @GET("aa4e2939-c546-43f8-bd71-32acdee83b9d")
    suspend fun getHotels(): HotelResponse
}
