package com.example.commerce.payment.doamin

import com.example.commerce.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transaction_history")
class TransactionHistoryEntity(
    @Enumerated(EnumType.STRING)
    val type : TransactionType,
    val userId: Long,
    val orderId: Long,
    val paymentId: Long,
    val externalPaymentKey: String,
    val amount: BigDecimal,
    val message: String,
    val occurredAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseEntity(){
}
