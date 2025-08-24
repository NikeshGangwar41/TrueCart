package com.nikesh.truecart.data.repository

import com.nikesh.truecart.data.api.FakeStoreApiService
import com.nikesh.truecart.data.model.Product

class ProductRepository(private val apiService: FakeStoreApiService) {
    suspend fun getProducts(): List<Product> {
        return apiService.getProducts()
    }

    suspend fun getCategories(): List<String> {
        return apiService.getCategories()
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        return apiService.getProductsByCategory(category)
    }
}
