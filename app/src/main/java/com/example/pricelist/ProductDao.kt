package com.example.pricelist

import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<Product>

    @Insert
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("UPDATE products SET name = :name WHERE id = :id")
    suspend fun update(id: Int, name: String)
}
