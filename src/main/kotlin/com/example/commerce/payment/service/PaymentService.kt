package com.example.commerce.payment.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.KAKAO_PAY_PAYMENT_FAIL
import com.example.commerce.common.exception.ErrorCode.ORDER_ALREADY_PAID
import com.example.commerce.common.exception.ErrorCode.ORDER_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.PAYMENT_AMOUNT_MISMATCH
import com.example.commerce.common.exception.ErrorCode.PAYMENT_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.PAYMENT_STATUS_INVALID
import com.example.commerce.common.exception.ErrorCode.PAYMENT_USER_MISMATCH
import com.example.commerce.order.domain.Order
import com.example.commerce.order.domain.OrderState
import com.example.commerce.order.repository.OrderRepository
import com.example.commerce.payment.client.KakaoPayClient
import com.example.commerce.payment.client.KakaoReadyResponse
import com.example.commerce.payment.doamin.PaymentEntity
import com.example.commerce.payment.doamin.PaymentMethod
import com.example.commerce.payment.doamin.PaymentState
import com.example.commerce.payment.doamin.TransactionHistoryEntity
import com.example.commerce.payment.doamin.TransactionType
import com.example.commerce.payment.dto.response.CreatePaymentResponse
import com.example.commerce.payment.dto.response.PaymentApproveResponse
import com.example.commerce.payment.repository.PaymentRepository
import com.example.commerce.payment.repository.TransactionHistoryRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val kakaoPayClient: KakaoPayClient
) {

    // 결제 준비
    // 결제 준비 이후, 트랜잭션 밖에서 PG 사 결제 준비 API 호출
    // 클라이언트에 필요한 정보만 반환
    @Transactional
    fun createPayment(
        order: Order
    ): CreatePaymentResponse {
        val payment = paymentRepository.findByOrderId(order.id)

        if(payment != null){
            if (payment.state == PaymentState.SUCCESS) {
                throw CustomException(ORDER_ALREADY_PAID)
            }
            if(payment.state == PaymentState.READY){
                return CreatePaymentResponse.of(payment, order.name)
            }
        }

        val newPayment = PaymentEntity(
            userId = order.userId,
            orderId = order.id,
            originAmount = order.totalPrice,
            paidAmount = order.totalPrice,
            state = PaymentState.READY
        )

        paymentRepository.save(newPayment)

        return CreatePaymentResponse.of(newPayment, order.name)
    }

    @Transactional
    fun readyPayment(
        order: Order
    ): KakaoReadyResponse {
        val payment = paymentRepository.findByOrderId(order.id)
            ?: throw CustomException(PAYMENT_NOT_FOUND)

        if (payment.state != PaymentState.READY) {
            throw CustomException(PAYMENT_STATUS_INVALID)
        }

        val readyResponse = try {
            kakaoPayClient.ready(
                orderId = payment.orderId,
                orderName = order.name,
                orderKey = order.key,
                userId = payment.userId,
                quantity = 0,
                totalAmount = payment.paidAmount
            )
        } catch (e: HttpClientErrorException) {
            throw CustomException(KAKAO_PAY_PAYMENT_FAIL, "카카오페이 에러: ${e.responseBodyAsString}")
        } catch (e: RestClientException) {
            throw CustomException(KAKAO_PAY_PAYMENT_FAIL, e.message)
        }

        payment.ready(readyResponse.tid)

        return readyResponse
    }

    // 결제 승인
    @Transactional
    fun success(
        orderKey: String,
        userId: Long,
        amount: BigDecimal,
        pgToken: String,
    ): PaymentApproveResponse {
        val order = orderRepository.findByOrderKeyAndState(orderKey, OrderState.CREATED)
            .orElseThrow { CustomException(ORDER_NOT_FOUND) }

        val payment = paymentRepository.findByOrderId(order.id!!)
            ?: throw CustomException(PAYMENT_NOT_FOUND)

        // 검증
        if (payment.userId != order.userId) throw CustomException(PAYMENT_USER_MISMATCH)
        if (payment.state != PaymentState.READY) throw CustomException(PAYMENT_STATUS_INVALID)
        if (payment.paidAmount != amount) throw CustomException(PAYMENT_AMOUNT_MISMATCH)

        //TODO() PG 사 승인 API 호출(중요: 트랜잭션 커밋 전)
        val paymentApproveResponse = try {
            kakaoPayClient.approve(
                tid = payment.externalPaymentKey!!,
                orderId = payment.orderId,
                userId = payment.userId,
                pgToken = pgToken
            )
        } catch (e: HttpClientErrorException) {
            throw CustomException(KAKAO_PAY_PAYMENT_FAIL, "카카오페이 에러: ${e.responseBodyAsString}")  // 상세 정보 포함
        } catch (e: RestClientException) {
            throw CustomException(KAKAO_PAY_PAYMENT_FAIL, e.message)
        }

        //승인 성공 시에만 상태 변경
        payment.success(
            payment.externalPaymentKey!!,
            PaymentMethod.ACCOUNT,
            "pg 승인 값 기반으로",
            LocalDateTime.parse(paymentApproveResponse.approved_at),
        )

        order.paid()

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT,
                userId = order.userId,
                orderId = order.id!!,
                paymentId = payment.id!!,
                externalPaymentKey = payment.externalPaymentKey!!,
                amount = payment.paidAmount,
                message = "결제 성공",
                occurredAt = payment.paidAt!!
            ),
        )

        return PaymentApproveResponse.from(paymentApproveResponse)
    }

    @Transactional
    fun fail(
        orderKey: String,
        errorCode: String,
        errorMessage: String
    ) {
        val order = orderRepository.findByOrderKeyAndState(orderKey, OrderState.CREATED)
            .orElseThrow { CustomException(ORDER_NOT_FOUND) }

        val payment = paymentRepository.findByOrderId(order.id!!)
            ?: throw CustomException(PAYMENT_NOT_FOUND)

        // 실패 처리
        payment.fail()

        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                type = TransactionType.PAYMENT_FAIL,
                userId = order.userId,
                orderId = order.id!!,
                paymentId = payment.id!!,
                externalPaymentKey = payment.externalPaymentKey ?: "",
                amount = payment.paidAmount,
                message = "[$errorCode] $errorMessage",
                occurredAt = LocalDateTime.now()
            )
        )
    }
}
