package com.example.commerce.cart.repository

import com.example.commerce.cart.domain.CartItem
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItem, Long> {

    fun findByUserIdAndProductId(userId: Long, productId: Long): CartItem?
    fun findByUserId(userId: Long): List<CartItem>
}
