package com.example.myapplication.network

import com.example.myapplication.ui.UiItem

data class OrdersResponse(
    val orders: List<Int>
)

data class OrderResponse(
    val items: List<DeliveryItem>
)

data class DeliveryItem(
    val id: Int,
    val name: String,
    val count: Int
)

fun DeliveryItem.toUiItem(index: Int): UiItem {
    return UiItem(
        id = "$index-${this.id}",
        name = this.name,
        count = this.count
    )
}