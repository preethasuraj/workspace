package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Transaction
    @Query("Select * from Cart where id = :cartId")
    suspend fun getCartDetails(cartId: String): CartDetails

    @Transaction
    @Query("Select * from Cart")
    fun getCartDetails(): Flow<List<CartDetails>>

    @Transaction
    @Insert(entity = CartEntity::class, onConflict = REPLACE)
    suspend fun addCartDEntities(cartEntity: CartEntity)

    @Transaction
    @Insert(entity = Product::class, onConflict = REPLACE)
    suspend fun addProduct(details: List<Product>)

    @Transaction
    @Insert(entity = Product::class, onConflict = REPLACE)
    suspend fun addCartDetails(cartEntity: CartEntity, details: List<Product>) {
        addCartDEntities(cartEntity)
        addProduct(details)
    }
}