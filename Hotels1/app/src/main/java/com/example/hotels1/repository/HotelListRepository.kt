package com.example.hotels1.repository

import android.content.Context
import com.example.hotels1.local.Hotel
import com.example.hotels1.local.HotelResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HotelListRepository @Inject constructor(
    val gson: Gson,
    @ApplicationContext val context: Context,
) {
    private val json = context.assets.open("Hotels.json")
        .bufferedReader()
        .use{it.readText()}

    fun getHotels(): List<Hotel> {
        return gson.fromJson(json, HotelResponse::class.java).hotels
    }

    fun getDetails(id: String?): Hotel {
        return gson.fromJson(json, HotelResponse::class.java).hotels.first{
            it.id == id
        }
    }

}