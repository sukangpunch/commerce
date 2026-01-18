package com.example.commerce.payment.dto.request

import java.math.BigDecimal

data class PaymentSuccessRequest(
    val orderKey: String,
    val userId: Long,
    val amount: BigDecimal,
    val pgToken: String,
)
