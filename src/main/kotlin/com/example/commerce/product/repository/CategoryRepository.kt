package com.example.commerce.product.repository

import com.example.commerce.product.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {

    fun countByIdIn(categoryIds: Set<Long>): Int
    fun existsByName(name: String): Boolean
    fun findByIdIn(categoryIds: Set<Long>): List<Category>
}
