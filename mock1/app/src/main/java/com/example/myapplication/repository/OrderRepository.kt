package com.example.myapplication.repository

import com.example.myapplication.network.NetworkApi
import com.example.myapplication.network.toUiItem
import com.example.myapplication.ui.UiItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class OrderRepository @Inject constructor(
    val api: NetworkApi
) {

    suspend fun getOrders(): Result<List<UiItem>>{

            val orders = api.getOrders().orders

           val response =coroutineScope {
               orders.map { id ->
                   async {
                       try {
                           api.getOrder(id)
                       } catch (e: Exception) {
                           null
                       }

                   }
               }.awaitAll().filterNotNull()
           }

        val result = response.flatMapIndexed { index, ord ->
            ord.items.map {
                it.toUiItem(index)
            }
        }
        return Result.success(result)
    }
}