package com.example.commerce.product.repository

import com.example.commerce.product.domain.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long> {

    fun findByIdIn(ids: Collection<Long>): List<ProductEntity>
}
