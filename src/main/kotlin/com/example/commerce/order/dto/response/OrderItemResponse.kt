package com.example.commerce.order.dto.response

import java.math.BigDecimal

data class OrderItemResponse(
    val productId: Long,
    val productName: String,
    val thumbnailUrl: String,
    val shortDescription: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
)
