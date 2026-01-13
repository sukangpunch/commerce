package com.example.commerce.order.repository

import com.example.commerce.order.domain.Order
import com.example.commerce.order.domain.OrderState
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface OrderRepository : JpaRepository<Order, Long> {

    fun findByUserIdAndStateOrderByIdDesc(userId: Long, state: OrderState): List<Order>
    fun findByKeyAndState(key: String, state: OrderState): Optional<Order>
}
