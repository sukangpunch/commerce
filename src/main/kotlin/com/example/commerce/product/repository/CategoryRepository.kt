package com.example.commerce.product.repository

import com.example.commerce.product.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {
}