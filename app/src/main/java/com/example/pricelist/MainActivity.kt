package com.example.pricelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pricelist.ui.theme.ShoppingListScr

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShoppingListScr(ProductDB.instance(applicationContext).productDao())
        }
    }
}



