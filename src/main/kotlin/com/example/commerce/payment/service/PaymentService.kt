package com.example.commerce.payment.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.ORDER_ALREADY_PAID
import com.example.commerce.order.domain.Order
import com.example.commerce.order.repository.OrderRepository
import com.example.commerce.payment.doamin.PaymentEntity
import com.example.commerce.payment.doamin.PaymentState
import com.example.commerce.payment.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
) {

    @Transactional
    fun createPayment(
        order: Order
    ): Long {
        if(paymentRepository.findByOrderId(order.id)?.state == PaymentState.SUCCESS){
            throw CustomException(ORDER_ALREADY_PAID)
        }

        val payment = PaymentEntity(
            userId = order.userId,
            orderId = order.id,
            originAmount = order.totalPrice,
            paidAmount = order.totalPrice,
            state = PaymentState.READY
        )

        paymentRepository.save(payment)

        return payment.id!!
    }


}
