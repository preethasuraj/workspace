package com.example.restaurantapp.repository

import android.content.Context
import com.example.restaurantapp.local.Category
import com.example.restaurantapp.local.LocalEntity
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val gson: Gson,
    @ApplicationContext private val context: Context

) {
    private val json = context.assets.open("menu.json")
        .bufferedReader()
        .use { it.readText() }

    fun getMenu(): Result<List<Category>> {
        return Result.success(
            gson.fromJson(json, LocalEntity::class.java).categories
        )

    }
}

//data class DomainCategory(
//
//)