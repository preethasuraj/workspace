package com.example.myapplication.local

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class Module {
@Provides
@Singleton
fun provideGson(): Gson {
    return Gson()
}
}