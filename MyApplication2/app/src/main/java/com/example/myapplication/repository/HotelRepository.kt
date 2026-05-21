package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.localsource.Hotel
import com.example.myapplication.localsource.HotelResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HotelRepository @Inject constructor(
    val gson: Gson,
    @ApplicationContext val context: Context,
){

    fun loadHotels(): List<Hotel> {
        val json = context.assets.open("Hotels.json")
            .bufferedReader()
            .use{it.readText()}
        return gson.fromJson(json, HotelResponse::class.java).hotels
    }
}