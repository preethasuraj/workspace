package com.example.myapplication

import com.example.myapplication.details.DetailsViewModel
import com.example.myapplication.list.UserViewModel
import com.example.myapplication.network.ApiService
import com.example.myapplication.repository.UserRepository
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {  }
    //viewModel {  } // get() automatically resolves dependencies
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }
}

val dataModule = module {
    single {
        UserRepository(get())
    }
}

val viewModelModule = module {
    viewModel { UserViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}