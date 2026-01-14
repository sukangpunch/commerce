package com.example.commerce.pg.doamin

import java.math.BigDecimal
import java.time.ZonedDateTime

data class PaymentApproveInfo(
    val tid: String,
    val paymentMethodType: String,
    val approvedAt: ZonedDateTime,
    val totalAmount: BigDecimal
) {
}
