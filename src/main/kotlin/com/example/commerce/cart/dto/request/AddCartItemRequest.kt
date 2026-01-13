package com.example.commerce.cart.dto.request

import com.example.commerce.cart.domain.AddCartItem
import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.*

data class AddCartItemRequest(
    val productId: Long,
    val quantity: Int,
) {

    fun toAddCartItem(): AddCartItem {
        if (quantity <= 0) throw CustomException(INVALID_PRODUCT_STOCK_QUANTITY)
        return AddCartItem(productId, quantity)
    }
}
