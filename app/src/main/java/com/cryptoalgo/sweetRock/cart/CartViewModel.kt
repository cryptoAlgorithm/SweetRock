package com.cryptoalgo.sweetRock.cart

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.cryptoalgo.sweetRock.model.CatalogItem

class CartViewModel: ViewModel() {
    data class CartItem(val food: CatalogItem, val quantity: Int)

    val cart = mutableStateListOf<CartItem>()

    companion object {
        private const val TAG = "CartViewModel"
    }

    fun addDish(item: CatalogItem) {
        if (inCart(item)) {
            Log.w(TAG, "Item ${item.id} already present in cart, ignoring")
            return
        }
        cart.add(CartItem(item, 1))
        Log.d(TAG, "Added item, current cart: ${cart.toList()}")
    }

    fun removeDish(item: CartItem) {
        cart.remove(item)
    }

    fun changeQuantity(item: CartItem, quantity: Int) {
        val idx = cart.indexOfFirst { it.food.id == item.food.id }
        cart[idx] = CartItem(item.food, quantity)
    }

    fun inCart(item: CatalogItem): Boolean = cart.any { it.food.id == item.id }

    val totalPrice: Float get() {
        var sum = 0f
        cart.forEach { sum += it.food.price*it.quantity }
        return sum
    }
}