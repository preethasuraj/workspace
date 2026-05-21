package com.example.myapplication.remote

data class UserResponse(
    val users: List<User>
)

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val image: String,
)