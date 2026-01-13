package com.example.commerce.order.domain

import java.math.BigDecimal

class OrderItem(
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val thumbnailUrl: String,
    val shortDescription: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
) {
}
