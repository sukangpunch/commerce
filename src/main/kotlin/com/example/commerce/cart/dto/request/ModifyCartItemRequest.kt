package com.example.commerce.cart.dto.request

import com.example.commerce.cart.domain.ModifyCartItem

data class ModifyCartItemRequest(
    val quantity: Int
){
    fun toModifyCartItem(cartItemId: Long): ModifyCartItem {
        return ModifyCartItem(cartItemId, quantity)
    }
}
