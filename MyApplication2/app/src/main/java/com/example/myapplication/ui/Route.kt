package com.example.myapplication.ui

sealed class Route(val name: String) {
    data object List: Route("List")
    data object Details: Route("List/{hotelId}") {
        fun createRoute(id: String): String {
            return "List/id"
        }
    }
}