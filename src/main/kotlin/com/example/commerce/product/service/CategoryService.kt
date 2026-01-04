package com.example.commerce.product.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.CATEGORY_NAME_DUPLICATED
import com.example.commerce.common.exception.ErrorCode.CATEGORY_NOT_FOUND
import com.example.commerce.product.domain.Category
import com.example.commerce.product.dto.request.CategoryCreateRequest
import com.example.commerce.product.dto.request.CategoryIdsRequest
import com.example.commerce.product.dto.response.CategoryResponse
import com.example.commerce.product.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    @Transactional
    fun addCategory(request: CategoryCreateRequest) : CategoryResponse {
        if(categoryRepository.existsByName(request.name)){
            throw CustomException(CATEGORY_NAME_DUPLICATED)
        }
        val category = Category(request.name)

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

    @Transactional(readOnly = true)
    fun findCategoriesByProduct(request: CategoryIdsRequest): List<CategoryResponse> {
        val categoryIds = request.categoryIds
        validateCategoryIds(categoryIds)

        val categories = categoryRepository.findByIdIn(categoryIds)

        return categories.map {category ->
            CategoryResponse(category.id!!, category.name)
        }.toList()
    }

    private fun validateCategoryIds(categoryIds: Set<Long>) {
        val categoryCount = categoryRepository.countByIdIn(categoryIds)
        if (categoryIds.size != categoryCount) {
            throw CustomException(CATEGORY_NOT_FOUND)
        }
    }
}