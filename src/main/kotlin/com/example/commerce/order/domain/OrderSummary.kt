package com.example.commerce.order.domain

import java.math.BigDecimal

data class OrderSummary(
    val id: Long,
    val orderKey: String,
    val name: String,
    val userId: Long,
    val totalPrice: BigDecimal,
    val state: OrderState,
)
