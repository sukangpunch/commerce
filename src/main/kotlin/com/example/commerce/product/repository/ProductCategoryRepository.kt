package com.example.commerce.product.repository

import com.example.commerce.product.domain.ProductCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductCategoryRepository : JpaRepository<ProductCategoryEntity, Long> {

    fun deleteByProductId(productId: Long)

    fun findByProductId(productId: Long): List<ProductCategoryEntity>
}
