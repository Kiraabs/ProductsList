package com.example.pricelist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductDB : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: ProductDB? = null

        fun instance(context: Context): ProductDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ProductDB::class.java, "product_db").build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun productDao(): ProductDao
}
