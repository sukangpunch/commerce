package com.example.commerce.pg.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_APPROVE_RESPONSE_EMPTY
import com.example.commerce.common.exception.ErrorCode.PAYMENT_NOT_FOUND
import com.example.commerce.payment.repository.PaymentRepository
import com.example.commerce.pg.config.KakaoPayProperties
import com.example.commerce.pg.dto.request.PaymentRequest
import com.example.commerce.pg.dto.response.KakaoApproveResponse
import com.example.commerce.pg.dto.response.KakaoReadyResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDateTime

@Slf4j
@Service
class KakaoPayService(
    private val kakoPayProperties: KakaoPayProperties,
    private val paymentRepository: PaymentRepository,
    private val kakaoPayProperties: KakaoPayProperties,
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private val restTemplate = RestTemplate()
    private var kakaoReadyResponse: KakaoReadyResponse? = null

    private fun getHeader(): HttpHeaders {
        return HttpHeaders().apply {
            set("Authorization", "SECRET_KEY ${kakoPayProperties.secretKey}")
            set("Content-Type", "application/json")
        }
    }

    @Transactional
    fun kakaoPayReady(request: PaymentRequest): KakaoReadyResponse {
        val orderId = request.orderId.toString()
        val userId = request.userId.toString()
        val itemName = "주문" // 추후 추가
        val price = request.totalPrice
        val vatAmount = (request.totalPrice.divide(BigDecimal.TEN)).toString()

        log.info("cid : {}", kakaoPayProperties.cid)
        val params = mapOf(
            "cid" to kakoPayProperties.cid,
            "partner_order_id" to orderId,
            "partner_user_id" to userId,
            "item_name" to itemName,
            "quantity" to "1",
            "total_amount" to price,
            "vat_amount" to vatAmount,
            "tax_free_amount" to "0",
            "approval_url" to "http://localhost:8080/api/v1/kakao-pay/success?orderId=$orderId",
            "fail_url" to "http://localhost:8080/api/v1/kakao-pay/fail",
            "cancel_url" to "http://localhost:8080/api/v1/kakao-pay/cancel"
        )

        val requestEntity = HttpEntity(params, getHeader())

        kakaoReadyResponse = restTemplate.postForObject(
            "https://open-api.kakaopay.com/online/v1/payment/ready",
            requestEntity,  // 요청 바디
            KakaoReadyResponse::class.java // 응답을 매핑할 클래스
        )
        return kakaoReadyResponse!!
    }

    @Transactional
    fun approveResponse(
        pgToken: String,
        orderId: Long,
    ): KakaoApproveResponse {
        val payment = paymentRepository.findByOrderId(orderId)
            ?: throw CustomException(PAYMENT_NOT_FOUND)

        log.info("payment 조회 완료")

        // 카카오 요청
        val params = mapOf(
            "cid" to kakaoPayProperties.cid,
            "tid" to kakaoReadyResponse!!.tid,
            "partner_order_id" to orderId.toString(),
            "partner_user_id" to payment.userId.toString(),
            "pg_token" to pgToken
        )

        // 헤더
        val requestEntity = HttpEntity(params, getHeader())

        try{
            val approve = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse::class.java
            ) ?: throw CustomException(KAKAO_PAY_APPROVE_RESPONSE_EMPTY)

            log.info("approve 결과")

            val tid = approve.tid
            val paymentMethodType = approve.payment_method_type
            val approvedAt = LocalDateTime.parse(approve.approved_at)
            val totalAmount = approve.amount.total
            return approve
        } catch (e: RestClientException) {
            log.error("=== RestClient 일반 에러 ===", e)
            throw CustomException(ErrorCode.KAKAO_PAY_HTTP_ERROR)
        }
    }
}
