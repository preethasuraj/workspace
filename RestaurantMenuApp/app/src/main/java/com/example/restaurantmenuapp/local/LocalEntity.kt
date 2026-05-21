package com.example.restaurantmenuapp.local

import androidx.compose.runtime.Immutable

data class LocalEntity (
    val categories: List<Category>
)

data class  Category(
    val id: String,
    val name:String,
    val items: List<Item>,
)

data class Item(
    val id: String,
    val name:String,
    val price:String,
    val image:String,
)
