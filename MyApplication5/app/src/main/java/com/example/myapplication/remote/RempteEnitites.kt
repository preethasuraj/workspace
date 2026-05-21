package com.example.myapplication.remote

data class CartResponse(
    val carts: List<Cart>
)

data class Cart(
    val id: Int
)

data class CartDetails(
    val id: Int,
    val products: List<Product>,
    val total: Double,
    val totalProducts: Int,
)

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val qty: Int,
    val discountPercentage: Double,
    val thumbnail : String
)