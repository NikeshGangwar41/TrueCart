package com.nikesh.truecart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nikesh.truecart.data.model.Product
import com.nikesh.truecart.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val repository: ProductRepository, private val productId: Int) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchProductDetail()
    }

    private fun fetchProductDetail() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val products = repository.getProducts()
                _product.value = products.find { it.id == productId }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }
}

class ProductDetailViewModelFactory(private val repository: ProductRepository, private val productId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailViewModel(repository, productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
