package com.nikesh.truecart.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nikesh.truecart.data.model.Product
import com.nikesh.truecart.data.model.WishlistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WishlistViewModel : ViewModel() {
    private val _wishlistItems = MutableStateFlow<List<WishlistItem>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItem>> = _wishlistItems.asStateFlow()

    fun addProductToWishlist(product: Product) {
        _wishlistItems.update { 
            if (it.none { item -> item.product.id == product.id }) {
                it + WishlistItem(product)
            } else {
                it
            }
        }
    }

    fun removeProductFromWishlist(product: Product) {
        _wishlistItems.update { currentItems ->
            currentItems.filter { it.product.id != product.id }
        }
    }
}
