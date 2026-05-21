package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val users: List<User>
)

data class User (
    val id: Int,
    @SerializedName("firstName")
    val first:String,
    @SerializedName("lastName")
    val last:String,
)

data class Posts(
    val id: Int,
    val userId: String,
    val title: String,
    val tags: List<String>,
)