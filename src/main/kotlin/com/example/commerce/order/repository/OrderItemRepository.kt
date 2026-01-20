package com.example.commerce.order.repository

import com.example.commerce.order.domain.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItemEntity, Long> {

    fun findByOrderId(orderId: Long): List<OrderItemEntity>
}
