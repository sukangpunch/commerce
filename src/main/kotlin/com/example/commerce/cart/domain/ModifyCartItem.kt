package com.example.commerce.cart.domain

data class ModifyCartItem(
    val cartItemId: Long,
    val quantity: Int,
) {
}
