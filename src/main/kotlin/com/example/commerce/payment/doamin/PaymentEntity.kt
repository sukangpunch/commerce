package com.example.commerce.payment.doamin

import com.example.commerce.common.BaseEntity
import com.example.commerce.order.domain.OrderEntity
import com.example.commerce.user.domain.UserEntity
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
import java.time.LocalDateTime

@Entity
@Table(name = "payment")
class PaymentEntity(
    @Column(name = "origin_amount")
    val originAmount: BigDecimal,

    @Column(name = "paid_amount")
    val paidAmount: BigDecimal,

    state: PaymentState,

    paidAt: LocalDateTime? = null,           // 이것도 pg 사

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val order: OrderEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseEntity() {

    @Enumerated(EnumType.STRING)
    var state: PaymentState = state
        protected set

    var paidAt: LocalDateTime? = paidAt
        protected set
}
