package com.example.commerce.order.dto.response

import com.example.commerce.order.domain.Order
import com.example.commerce.order.domain.OrderState
import java.math.BigDecimal

data class OrderResponse(
    val id: Long,
    val key: String,
    val name: String,
    val totalPrice: BigDecimal,
    val state: OrderState,
    val items: List<OrderItemResponse>,
){
    companion object {
        fun of(order: Order): OrderResponse {
            return OrderResponse(
                id = order.id,
                key = order.key,
                name = order.name,
                totalPrice = order.totalPrice,
                state = order.state,
                items = order.items.map {
                    OrderItemResponse(
                        productId = it.productId,
                        productName = it.productName,
                        thumbnailUrl = it.thumbnailUrl,
                        shortDescription = it.shortDescription,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        totalPrice = it.totalPrice,
                    )
                },
            )
        }
    }
}
