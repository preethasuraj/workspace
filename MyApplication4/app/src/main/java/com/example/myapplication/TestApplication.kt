package com.example.myapplication

import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MyApp", "onCreate hit")
        startKoin {
            // Log Koin events
            androidLogger()
            // Reference the Android context
            androidContext(this@TestApplication)
            // Load modules
            modules(appModule, networkModule, dataModule, viewModelModule)
        }
        Log.d("MyApp", "Koin started")
    }
}