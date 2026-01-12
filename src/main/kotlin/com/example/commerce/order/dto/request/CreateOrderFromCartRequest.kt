package com.example.commerce.order.dto.request

data class CreateOrderFromCartRequest(
    val cartItemIds: Set<Long>
)
