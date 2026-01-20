package com.example.commerce.payment.dto.request

data class PaymentFailRequest(
    val orderKey: String,
    val errorCode: String,
    val errorMessage: String,
)
