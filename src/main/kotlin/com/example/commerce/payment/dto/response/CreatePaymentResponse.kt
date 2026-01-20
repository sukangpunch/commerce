package com.example.commerce.payment.dto.response

import com.example.commerce.payment.doamin.PaymentEntity
import com.example.commerce.payment.doamin.PaymentState
import java.math.BigDecimal

data class CreatePaymentResponse(
    val paymentId: Long,
    val userId: Long,
    val orderId: Long,
    val productName: String,
    val paidAmount: BigDecimal,
    val state: PaymentState,
) {

    companion object {

        fun of(payment: PaymentEntity, productName: String): CreatePaymentResponse {
            return CreatePaymentResponse(
                paymentId = payment.id!!,
                userId = payment.userId,
                orderId = payment.orderId,
                productName = productName,
                paidAmount =  payment.paidAmount,
                state = payment.state,
            )
        }
    }
}
