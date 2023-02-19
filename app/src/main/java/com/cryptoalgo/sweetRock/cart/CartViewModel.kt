package com.cryptoalgo.sweetRock.cart

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.cryptoalgo.sweetRock.catalog.CatalogViewModel

class CartViewModel: ViewModel() {
    val cart = mutableStateListOf<CatalogViewModel.CatalogItem>()

    companion object {
        private const val TAG = "CartViewModel"
    }

    fun addDish(item: CatalogViewModel.CatalogItem) {
        cart.add(item)
        Log.d(TAG, "Added item, current cart: ${cart.toList()}")
    }

    fun inCart(item: CatalogViewModel.CatalogItem): Boolean = cart.contains(item)

    val totalPrice: Float get() {
        var sum = 0f
        cart.forEach { sum += it.price }
        return sum
    }
}