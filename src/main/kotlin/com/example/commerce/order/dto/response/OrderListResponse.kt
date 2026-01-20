package com.example.commerce.order.dto.response

import com.example.commerce.order.domain.OrderState
import com.example.commerce.order.domain.OrderSummary
import java.math.BigDecimal

data class OrderListResponse(
    val key: String,
    val name: String,
    val totalPrice: BigDecimal,
    val state: OrderState
) {

    companion object {

        private fun of(order: OrderSummary): OrderListResponse {
            return OrderListResponse(
                key = order.orderKey,
                name = order.name,
                totalPrice = order.totalPrice,
                state = order.state
            )
        }

        fun of(orders: List<OrderSummary>): List<OrderListResponse> { // 리스트 변환만 외부에 노출, 내부적으로 처리
            return orders.map { of(it) }
        }
    }
}
