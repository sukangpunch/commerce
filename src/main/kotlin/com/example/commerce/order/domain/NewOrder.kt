package com.example.commerce.order.domain

data class NewOrder(
    val userId: Long,
    val items: List<NewOrderItem>
)
