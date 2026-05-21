package com.example.myapplication.repository

import com.example.myapplication.database.CartDao
import com.example.myapplication.database.CartEntity
import com.example.myapplication.database.Product
import com.example.myapplication.remote.ApiService
import com.example.myapplication.remote.CartDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CartRepository @Inject constructor(
    val apiService: ApiService,
    val dao: CartDao,
) {

    val cartDetails = dao.getCartDetails()

    suspend fun getCartDetails(): Result<Unit> {
        return coroutineScope {
            try {

                val details = apiService.getCart().carts
                    .map {
                        async {
                            try {
                                apiService.getCartDetails(it.id)
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }.awaitAll().filterNotNull()
                details.forEach { cartDetails ->
                    val entity = CartEntity(
                        id = cartDetails.id,
                        total = cartDetails.total,
                        totalProducts = cartDetails.totalProducts
                    )
                    val products = cartDetails.products.map {
                        Product(
                            id = it.id,
                            title = it.title,
                            price = it.price,
                            qty = it.qty,
                            discountPercentage = it.discountPercentage,
                            thumbnail = it.thumbnail,
                            cartId = cartDetails.id
                        )
                    }

                    dao.addCartDetails(entity, products)
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure<Unit>(e)
            }
        }
    }
}
