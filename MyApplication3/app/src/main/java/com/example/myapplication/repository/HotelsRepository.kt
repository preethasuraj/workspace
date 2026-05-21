package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.List.HotelUiEntity
import com.example.myapplication.local.Hotel
import com.example.myapplication.local.HotelResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HotelsRepository @Inject constructor(
    private val gson: Gson,
    @ApplicationContext private val context: Context,
) {

    private  val json = context.assets.open("Hotels.json")
        .bufferedReader()
        .use { it.readText() }
    fun getHotelsFromLocal(): Result<List<Hotel>> {


        val result = gson.fromJson(json, HotelResponse::class.java)
            .hotels
        return Result.success(result)
    }

    fun getHotel(id: String): HotelUiEntity? {
        val hotel = gson.fromJson(json, HotelResponse::class.java)
            .hotels.first { it.id == id }
        return HotelUiEntity(
            id = hotel.id,
            name = hotel.name,
            city = hotel.city,
            description = hotel.description,
            starRating = "${hotel.starRating} *",
            images = hotel.images
        )
    }

}