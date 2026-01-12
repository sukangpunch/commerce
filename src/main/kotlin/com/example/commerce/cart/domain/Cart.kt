package com.example.commerce.cart.domain

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.CART_ITEM_NOT_FOUND
import com.example.commerce.order.domain.NewOrder
import com.example.commerce.order.domain.NewOrderItem

data class Cart(
    val userId: Long,
    val items: List<CartItemSummary>,
) {
    fun toNewOrder(targetItemIds: Set<Long>): NewOrder {
        if (items.isEmpty()) throw CustomException(CART_ITEM_NOT_FOUND)
        return NewOrder(
            userId = userId,
            items = items.filter { targetItemIds.contains(it.id) }
                .map {
                    NewOrderItem(
                        productId = it.productId,
                        quantity = it.quantity
                    )
                }
        )
    }
}
