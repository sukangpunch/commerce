package com.example.commerce.pg.dto.request

import java.math.BigDecimal

data class PaymentRequest(
    val userId: Long,
    val orderId: Long,
    val totalPrice: BigDecimal,
)
