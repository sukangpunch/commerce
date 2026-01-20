package com.example.commerce.cart.domain

import com.example.commerce.product.domain.Product

data class CartItem(
    val id: Long,
    val product: Product,
    val quantity: Int
)
