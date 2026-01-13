package com.example.commerce.payment.controller

import com.example.commerce.order.service.OrderService
import com.example.commerce.payment.dto.request.CreatePaymentRequest
import com.example.commerce.payment.dto.response.CreatePaymentResponse
import com.example.commerce.payment.service.PaymentService
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
        return ResponseEntity.ok(CreatePaymentResponse(1L))
    }
}
