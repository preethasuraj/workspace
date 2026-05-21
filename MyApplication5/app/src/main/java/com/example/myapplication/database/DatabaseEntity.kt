package com.example.myapplication.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Cart")
data class CartEntity(
    @PrimaryKey
    val id: Int,
    val total: Double,
    val totalProducts: Int,
)

@Entity(
    tableName = "Product",
    foreignKeys = [
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["id"],
            childColumns = ["cartId"]
        ),
    ],
    indices = [Index("cartId")]
)
data class Product(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val qty: Int,
    val discountPercentage: Double,
    val thumbnail: String,
    val cartId: Int
)

data class CartDetails(
    @Embedded
    val cartEntity: CartEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cartId"
    )
    val products: List<Product>
)