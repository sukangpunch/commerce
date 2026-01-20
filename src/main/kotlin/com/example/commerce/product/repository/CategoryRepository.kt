package com.example.commerce.product.repository

import com.example.commerce.product.domain.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<CategoryEntity, Long> {

    fun countByIdIn(categoryIds: Set<Long>): Int
    fun existsByName(name: String): Boolean
    fun findByIdIn(categoryIds: Set<Long>): List<CategoryEntity>
}
