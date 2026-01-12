package com.example.commerce.product.repository

import com.example.commerce.product.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByIdIn(ids: Collection<Long>): List<Product>
}
