package com.example.commerce.order.dto.request

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.ORDER_PRODUCT_QUANTITY_INVALID
import com.example.commerce.order.domain.NewOrder
import com.example.commerce.order.domain.NewOrderItem

data class CreateOrderRequest(
    val productId: Long,
    val quantity: Int
) {

    fun toNewOrder(userId: Long): NewOrder {
        if (quantity <= 0) throw CustomException(ORDER_PRODUCT_QUANTITY_INVALID)
        return NewOrder(
            userId = userId,
            items = listOf(NewOrderItem(productId, quantity))
        )
    }
}
