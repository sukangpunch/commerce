package com.example.commerce.pg.controller

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_PAYMENT_CANCEL
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_PAYMENT_FAIL
import com.example.commerce.pg.dto.request.PaymentRequest
import com.example.commerce.pg.dto.response.KakaoApproveResponse
import com.example.commerce.pg.dto.response.KakaoReadyResponse
import com.example.commerce.pg.service.KakaoPayService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/kakao-pay")
class KakaoPayController(
    private val kakaoPayService: KakaoPayService,
) {

    @PostMapping("/ready")
    fun readyToKakaoPay(@RequestBody request: PaymentRequest): ResponseEntity<KakaoReadyResponse> {
        val response = kakaoPayService.kakaoPayReady(request)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }

    @GetMapping("/success")
    fun afterPayRequest(
        @RequestParam("pg_token") pgToken: String,
        @RequestParam("orderId") orderId: Long,
    ): ResponseEntity<KakaoApproveResponse>
    {
        val status = HttpStatus.OK
        val response = kakaoPayService.approveResponse(pgToken, orderId)
        return ResponseEntity.status(status).body(response)
    }

    @GetMapping("/cancel")
    fun cancelPayment(){
        throw CustomException(KAKAO_PAY_PAYMENT_CANCEL)
    }

    @GetMapping("/fail")
    fun failPayment(){
        throw CustomException(KAKAO_PAY_PAYMENT_FAIL)
    }
}
