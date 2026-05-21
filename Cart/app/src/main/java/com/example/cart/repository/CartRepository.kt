package com.example.cart.repository

import javax.inject.Inject

class CartRepository @Inject constructor() {

    fun getCartData(): Result<CartDomainEntity> {
        return Result.success(
            getFeed()
        )

    }

    private fun getFeed(): CartDomainEntity {
        return CartDomainEntity(listOf(
            Item(
                id = "1",
                name= "item1",
                price = 10.00,
                qty = 1
            ),
            Item(
                id = "2",
                name= "item2",
                price = 10.00,
                qty = 1
            ),
            Item(
                id = "3",
                name= "item3",
                price = 10.00,
                qty = 2
            ),
            Item(
                id = "4",
                name= "item4",
                price = 10.00,
                qty = 2
            )
        )
        )
    }
}

data class CartDomainEntity(
    val items: List<Item>
)

data class Item(
    val id: String,
    val name: String,
    val qty: Int,
    val price: Double,

    )