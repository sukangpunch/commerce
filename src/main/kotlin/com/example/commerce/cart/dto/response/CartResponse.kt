package com.example.commerce.cart.dto.response

import java.math.BigDecimal

data class CartResponse(
    val userId: Long,
    val items: List<CartItemResponse>,
)

data class CartItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val price: BigDecimal,
    val quantity: Int,
)