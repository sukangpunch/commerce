package com.example.commerce.payment.repository

import com.example.commerce.payment.doamin.TransactionHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionHistoryRepository : JpaRepository<TransactionHistoryEntity, Long> {
}
