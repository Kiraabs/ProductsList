package com.example.pricelist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(@PrimaryKey(autoGenerate = true) val id: Int = 0, var name: String)
