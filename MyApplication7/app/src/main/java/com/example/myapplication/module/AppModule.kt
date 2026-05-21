package com.example.myapplication.module

import com.example.myapplication.details.DetailsViewModel
import com.example.myapplication.list.ListViewModel
import com.example.myapplication.remote.RemoteApi
import com.example.myapplication.repository.Repository
import com.example.myapplication.repository.UserRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteApi::class.java)
    }
    single<Repository> {
        UserRepository(get())
    }
    single {
        ListViewModel(get())
    }
    single {
        DetailsViewModel(get())
    }
}