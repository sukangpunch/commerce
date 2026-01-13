package com.example.commerce.order.repository

import com.example.commerce.order.domain.OrderEntity
import com.example.commerce.order.domain.OrderState
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface OrderRepository : JpaRepository<OrderEntity, Long> {

    fun findByUserIdAndStateOrderByIdDesc(userId: Long, state: OrderState): List<OrderEntity>
    fun findByOrderKeyAndState(orderKey: String, state: OrderState): Optional<OrderEntity>
}
