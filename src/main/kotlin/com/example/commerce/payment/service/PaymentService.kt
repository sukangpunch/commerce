package com.example.commerce.payment.service

import com.example.commerce.order.domain.OrderSummary
import com.example.commerce.order.repository.OrderRepository
import com.example.commerce.payment.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
) {

    @Transactional
    fun createPayment(order: OrderSummary): Long {
        return 1
    }
}
