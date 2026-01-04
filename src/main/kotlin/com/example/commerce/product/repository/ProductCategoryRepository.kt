package com.example.commerce.product.repository

import com.example.commerce.product.domain.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ProductCategoryRepository : JpaRepository<ProductCategory, Long> {
    fun deleteByProductId(productId: Long)

    fun findByProductId(productId: Long): List<ProductCategory>
}