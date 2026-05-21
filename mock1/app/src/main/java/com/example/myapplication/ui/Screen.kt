package com.example.myapplication.ui

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Details: Screen("details/{orderId}"){
        fun createRoute(id: String): String = "details/$id"
    }
}