package com.example.paging3.remote

data class ItemResponse(
    val items: List<Item>, val page: Int,
    val pageSize: Int,
    val totalCount: Int,
    val hasNext: Boolean

)

data class Item(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val rating: Float,
)
