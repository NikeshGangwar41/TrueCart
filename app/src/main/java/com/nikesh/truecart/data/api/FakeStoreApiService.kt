package com.nikesh.truecart.data.api

import com.nikesh.truecart.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<Product>
}
