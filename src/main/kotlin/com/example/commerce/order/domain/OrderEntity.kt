package com.example.commerce.order.domain

import com.example.commerce.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.Getter
import java.math.BigDecimal

@Entity
@Table(name = "orders")
@Getter
class OrderEntity(
    @Column(name = "order_key", nullable = false)
    val orderKey: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "total_price", nullable = false)
    var totalPrice: BigDecimal,

    state: OrderState,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseEntity() {

    @Enumerated(EnumType.STRING)
    var state: OrderState = state // 파라미터로 전달받은 state
        protected set

    fun paid() {
        state = OrderState.PAID
    }

    fun canceled() {
        state = OrderState.CANCELED
    }
}
