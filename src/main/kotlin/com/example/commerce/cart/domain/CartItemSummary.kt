package com.example.commerce.cart.domain

data class CartItemSummary(
    val id: Long,
    val productId: Long,
    val quantity: Int
)