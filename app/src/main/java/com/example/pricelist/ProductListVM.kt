package com.example.pricelist

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProductListVM : ViewModel() {
    private val _prods = mutableStateOf(listOf<Product>())
    val productList: State<List<Product>> = _prods

    fun add(prod: Product) {
        _prods.value += prod
    }

    fun remove(prod: Product) {
        _prods.value -= prod
    }

    fun replace(old: Product, new: Product, scope: CoroutineScope, dao: ProductDao) {
        _prods.value.find { it.id == old.id }!!.name = new.name
        scope.launch { dao.update(old.id, new.name) }
    }

    fun copyFrom(list: List<Product>) {
        list.forEach { _prods.value += it }
    }

    fun sort() {
        _prods.value = _prods.value.sortedBy { it.name }
    }
}