package com.example.commerce.order.dto.response

import com.example.commerce.order.domain.OrderState
import java.math.BigDecimal

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val key: String,
    val name: String,
    val totalPrice: BigDecimal,
    val state: OrderState,
    val items: List<OrderItemResponse>,
)
