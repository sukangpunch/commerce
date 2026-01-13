package com.example.commerce.order.domain

import com.example.commerce.common.BaseEntity
import com.example.commerce.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "orders")
class Order(
    @Column(name = "order_key", nullable = false)
    val key: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "total_price", nullable = false)
    var totalPrice: BigDecimal,

    state: OrderState,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

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
