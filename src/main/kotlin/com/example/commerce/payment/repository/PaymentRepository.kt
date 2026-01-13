package com.example.commerce.payment.repository

import com.example.commerce.payment.doamin.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<PaymentEntity, Long> {
}
