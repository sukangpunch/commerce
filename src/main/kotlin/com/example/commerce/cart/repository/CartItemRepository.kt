package com.example.commerce.cart.repository

import com.example.commerce.cart.domain.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItemEntity, Long> {

    fun findByUserIdAndProductId(userId: Long, productId: Long): CartItemEntity?
    fun findByUserId(userId: Long): List<CartItemEntity>
}
