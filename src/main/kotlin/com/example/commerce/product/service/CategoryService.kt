package com.example.commerce.product.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.CATEGORY_NAME_DUPLICATED
import com.example.commerce.common.exception.ErrorCode.PRODUCT_NOT_FOUND
import com.example.commerce.product.domain.CategoryEntity
import com.example.commerce.product.dto.request.CategoryCreateRequest
import com.example.commerce.product.dto.response.CategoryResponse
import com.example.commerce.product.repository.CategoryRepository
import com.example.commerce.product.repository.ProductCategoryRepository
import com.example.commerce.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val productCategoryRepository: ProductCategoryRepository
) {

    @Transactional
    fun addCategory(request: CategoryCreateRequest): CategoryResponse {
        if (categoryRepository.existsByName(request.name)) {
            throw CustomException(CATEGORY_NAME_DUPLICATED)
        }
        val category = CategoryEntity(request.name)

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
    fun findCategoriesByProduct(productId: Long): List<CategoryResponse> {
        val product = productRepository.findById(productId)
            .orElseThrow { CustomException(PRODUCT_NOT_FOUND) }

        val productCategories = productCategoryRepository.findByProductId(product.id!!)
        val categoryIds = productCategories.map { it.categoryId }.toSet()
        val categories = categoryRepository.findByIdIn(categoryIds)

        return categories.map { category ->
            CategoryResponse(category.id!!, category.name)
        }.toList()
    }
}
