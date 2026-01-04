package com.example.commerce.product.service

import com.example.commerce.product.domain.Category
import com.example.commerce.product.dto.response.CategoryResponse
import com.example.commerce.product.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    @Transactional
    fun addCategory(name: String) : CategoryResponse {
        val category = Category(name)

        categoryRepository.save(category)

        return CategoryResponse(
            category.id!!,
            category.name
        )
    }

    @Transactional(readOnly = true)
    fun findAllCategory(): List<CategoryResponse> {
        val categoryList = categoryRepository.findAll()
        return categoryList.map { category ->
            CategoryResponse(category.id!!, category.name)
        }
    }
}