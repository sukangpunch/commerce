package com.example.commerce.payment.controller

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_PAYMENT_CANCEL
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_PAYMENT_FAIL
import com.example.commerce.order.domain.OrderState
import com.example.commerce.order.service.OrderService
import com.example.commerce.payment.client.KakaoReadyResponse
import com.example.commerce.payment.dto.request.CreatePaymentReadyRequest
import com.example.commerce.payment.dto.request.CreatePaymentRequest
import com.example.commerce.payment.dto.request.PaymentSuccessRequest
import com.example.commerce.payment.dto.response.CreatePaymentResponse
import com.example.commerce.payment.dto.response.PaymentApproveResponse
import com.example.commerce.payment.service.PaymentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentService,
    private val orderService: OrderService
) {

    @PostMapping("/create")
    fun create(
        @RequestParam("userId") userId: Long,
        @RequestBody request: CreatePaymentRequest,
    ): ResponseEntity<CreatePaymentResponse> {
        val order = orderService.getOrder(userId, request.orderKey, OrderState.CREATED)
        val response = paymentService.createPayment(order)
        val status = HttpStatus.CREATED
        return ResponseEntity.status(status).body(response)
    }

    @PostMapping("/ready")
    fun readyPayment(
        @RequestParam userId: Long,
        @RequestBody request: CreatePaymentReadyRequest
    ): ResponseEntity<KakaoReadyResponse> {
        val order = orderService.getOrder(userId, request.orderKey, OrderState.CREATED)
        val response = paymentService.readyPayment(order)
        val status = HttpStatus.CREATED
        return ResponseEntity.status(status).body(response)
    }

    @PostMapping("/success")
    fun successPayment(
        @RequestParam userId: Long,
        @RequestBody request: PaymentSuccessRequest
    ): ResponseEntity<PaymentApproveResponse> {
        val response = paymentService.success(
            orderKey = request.orderKey,
            userId = request.userId,
            amount = request.amount,
            pgToken = request.pgToken,
        )
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }

    @PostMapping("/cancel")
    fun cancelPayment() {
        throw CustomException(KAKAO_PAY_PAYMENT_CANCEL)
    }

    @PostMapping("/fail")
    fun failPayment() {
        throw CustomException(KAKAO_PAY_PAYMENT_FAIL)
    }
}
