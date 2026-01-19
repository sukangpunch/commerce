package com.example.commerce.payment.client

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_APPROVE_RESPONSE_EMPTY
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_HTTP_ERROR
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_READY_RESPONSE_EMPTY
import com.example.commerce.payment.config.KakaoPayProperties
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Component
class KakaoPayClient(
    private val restTemplate: RestTemplate,
    private val kakaoPayProperties: KakaoPayProperties,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    private fun getHeader(): HttpHeaders {
        return HttpHeaders().apply {
            set("Authorization", "SECRET_KEY ${kakaoPayProperties.secretKey}")
            set("Content-Type", "application/json")
        }
    }

    fun ready(
        orderId: Long,
        orderName: String,
        userId: Long,
        quantity: Int,
        totalAmount: BigDecimal
    ): KakaoReadyResponse {
        val vatAmount = (totalAmount.divide(BigDecimal.TEN))
        val params = mapOf(
            "cid" to kakaoPayProperties.cid,
            "partner_order_id" to orderId,
            "partner_user_id" to userId,
            "item_name" to orderName,
            "quantity" to quantity,
            "total_amount" to totalAmount,
            "vat_amount" to vatAmount,
            "tax_free_amount" to "0",
            "approval_url" to "http://localhost:8080/api/v1/kakao-pay/success?orderId=$orderId",
            "fail_url" to "http://localhost:8080/api/v1/kakao-pay/fail",
            "cancel_url" to "http://localhost:8080/api/v1/kakao-pay/cancel"
        )

        val requestEntity = HttpEntity(params, getHeader())

        return try {
            restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,  // 요청 바디
                KakaoReadyResponse::class.java // 응답을 매핑할 클래스
            ) ?: throw CustomException(KAKAO_PAY_READY_RESPONSE_EMPTY)
        } catch (e: RestClientException) {
            log.error("카카오페이 승인 API 호출 실패", e)
            throw CustomException(KAKAO_PAY_HTTP_ERROR)
        }
    }

    fun approve(
        tid: String,
        orderId: Long,
        userId: Long,
        pgToken: String
    ): KakaoApproveResponse {
        val params = mapOf(
            "cid" to kakaoPayProperties.cid,
            "tid" to tid,
            "partner_order_id" to orderId,
            "partner_user_id" to userId,
            "pg_token" to pgToken
        )

        val requestEntity = HttpEntity(params, getHeader())

        return try {
            restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse::class.java
            ) ?: throw CustomException(KAKAO_PAY_APPROVE_RESPONSE_EMPTY)
        } catch (e: RestClientException) {
            log.error("카카오페이 승인 API 호출 실패", e)
            throw CustomException(KAKAO_PAY_HTTP_ERROR)
        }
    }
}
