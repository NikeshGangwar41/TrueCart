package com.nikesh.truecart.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nikesh.truecart.data.model.CartItem
import com.nikesh.truecart.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.nikesh.truecart.data.model.Order
import com.nikesh.truecart.data.model.OrderItem
import java.util.UUID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _newOrder = MutableSharedFlow<Order>()
    val newOrder: SharedFlow<Order> = _newOrder

    fun addProductToCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                currentItems.map { item ->
                    if (item.product.id == product.id) item.copy(quantity = item.quantity + 1) else item
                }
            } else {
                currentItems + CartItem(product, 1)
            }
        }
    }

    fun removeProductFromCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null && existingItem.quantity > 1) {
                currentItems.map { item ->
                    if (item.product.id == product.id) item.copy(quantity = item.quantity - 1) else item
                }
            } else {
                currentItems.filter { it.product.id != product.id }
            }
        }
    }

    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    fun getTotalCartPrice(): Double {
        return _cartItems.value.sumOf { it.product.price * it.quantity }
    }

    suspend fun checkout() {
        if (_cartItems.value.isNotEmpty()) {
            val orderItems = _cartItems.value.map { cartItem ->
                OrderItem(
                    product = cartItem.product,
                    quantity = cartItem.quantity,
                    priceAtOrder = cartItem.product.price
                )
            }
            val newOrder = Order(
                id = UUID.randomUUID().toString(),
                items = orderItems,
                totalAmount = getTotalCartPrice(),
                orderDate = System.currentTimeMillis()
            )
            _newOrder.emit(newOrder)
            _cartItems.value = emptyList()
        }
    }
}
