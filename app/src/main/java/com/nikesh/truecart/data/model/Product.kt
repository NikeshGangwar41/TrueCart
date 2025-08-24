package com.nikesh.truecart.data.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)

data class CartItem(
    val product: Product,
    var quantity: Int
)

data class WishlistItem(
    val product: Product
)

data class OrderItem(
    val product: Product,
    val quantity: Int,
    val priceAtOrder: Double
)

data class Order(
    val id: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val orderDate: Long
)
