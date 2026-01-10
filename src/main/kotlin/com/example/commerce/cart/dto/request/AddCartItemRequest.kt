package com.example.commerce.cart.dto.request

data class AddCartItemRequest(
    val productId: Long,
    val quantity: Int,
) {
}