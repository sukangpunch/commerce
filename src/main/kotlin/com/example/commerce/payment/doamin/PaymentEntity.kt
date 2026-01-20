package com.example.commerce.payment.doamin

import com.example.commerce.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
    name = "payment",
    indexes = [
        Index(name = "udx_order_id", columnList = "orderId", unique = true)
    ]
)
class PaymentEntity(

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "order_id")
    val orderId: Long,

    @Column(name = "origin_amount")
    val originAmount: BigDecimal,

    @Column(name = "paid_amount")
    val paidAmount: BigDecimal,

    state: PaymentState,
    externalPaymentKey: String? = null,      // 이게 pg 사에서 보내주는 키
    method: PaymentMethod? = null,           // 이것도 pg사
    approveCode: String? = null,             // 이것도 pg 사
    paidAt: LocalDateTime? = null,           // 이것도 pg 사

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseEntity() {

    @Enumerated(EnumType.STRING)
    var state: PaymentState = state
        protected set

    var externalPaymentKey: String? = externalPaymentKey
        protected set

    var method: PaymentMethod? = method
        protected set

    var approveCode: String? = approveCode
        protected set

    var paidAt: LocalDateTime? = paidAt
        protected set

    fun ready(externalPaymentKey: String){
        this.externalPaymentKey = externalPaymentKey
    }

    fun success(
        externalPaymentKey: String,
        method: PaymentMethod,
        approveCode : String,
        approveAt: LocalDateTime
    ){
        this.state = PaymentState.SUCCESS
        this.externalPaymentKey = externalPaymentKey
        this.method = method
        this.approveCode = approveCode
        this.paidAt = approveAt
    }

    fun fail(){
        this.state = PaymentState.FAIL
    }

    fun cancel(){
        this.state = PaymentState.CANCEL
    }
}
