package com.example.commerce.cart.domain

data class AddCartItem(
    val productId: Long,
    val quantity: Int
) {
}
