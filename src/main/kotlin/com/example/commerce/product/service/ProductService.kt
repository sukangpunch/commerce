package com.example.commerce.product.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.*
import com.example.commerce.product.domain.Product
import com.example.commerce.product.domain.ProductCategory
import com.example.commerce.product.dto.request.ProductCreateRequest
import com.example.commerce.product.dto.request.ProductUpdateRequest
import com.example.commerce.product.dto.response.ProductDetailResponse
import com.example.commerce.product.repository.CategoryRepository
import com.example.commerce.product.repository.ProductCategoryRepository
import com.example.commerce.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val productCategoryRepository: ProductCategoryRepository
) {
    companion object {
        private const val DEFAULT_IMAGE_URL = "http://test-image.jpg"
    }

    @Transactional
    fun createProduct(request: ProductCreateRequest): ProductDetailResponse {
        val categoryIds = request.categoryIds // 카테고리에 문제가 있으면 바로 예외
        validateCategoryIds(categoryIds)

        val product = Product(
            request.name,
            request.price,
            request.description,
            DEFAULT_IMAGE_URL,
            request.shortDescription,
            request.stockQuantity
        )
        productRepository.save(product)

        mappingCategoriesToProduct(product.id!!, categoryIds) // 카테고리 검증 이후이므로 안전하게 매핑

        return ProductDetailResponse(
            product.id!!,
            product.name,
            product.price,
            product.description,
            product.shortDescription,
            product.stockQuantity,
            product.imageUrl,
            categoryIds
        )
    }

    private fun validateCategoryIds(categoryIds: Set<Long>) {
        if (categoryIds.isEmpty()) {
            throw CustomException(CATEGORY_REQUIRED)
        }

        validateCategoryIdsAndCategories(categoryIds)
    }

    @Transactional
    fun updateProduct(
        id: Long,
        request: ProductUpdateRequest
    ): ProductDetailResponse {
        val product = productRepository.findById(id)
            .orElseThrow { CustomException(PRODUCT_NOT_FOUND) }
        product.update(
            request.name,
            request.price,
            request.description,
            request.shortDescription,
            request.stockQuantity
        )

        request.categoryIds?.let { newCategoryIds ->
            if (newCategoryIds.isNotEmpty()) {
                updateProductCategories(product.id!!, newCategoryIds)
            }
        }

        val currentCategoryIds = getCurrentCategoryIds(product.id!!)

        return ProductDetailResponse(
            product.id!!,
            product.name,
            product.price,
            product.description,
            product.shortDescription,
            product.stockQuantity,
            product.imageUrl,
            currentCategoryIds
        )
    }

    private fun updateProductCategories(productId: Long, newCategoryIds: Set<Long>) {
        validateCategoryIdsAndCategories(newCategoryIds)

        productCategoryRepository.deleteByProductId(productId)
        productCategoryRepository.flush()
        mappingCategoriesToProduct(productId, newCategoryIds)
    }

    private fun validateCategoryIdsAndCategories(newCategoryIds: Set<Long>) {
        val categoryCount = categoryRepository.countByIdIn(newCategoryIds)
        if (newCategoryIds.size != categoryCount) {
            throw CustomException(CATEGORY_NOT_FOUND)
        }
    }

    private fun mappingCategoriesToProduct(productId: Long, categoryIds: Set<Long>) {
        val productCategories = categoryIds.map { categoryId ->
            ProductCategory(productId, categoryId)
        }

        productCategoryRepository.saveAll(productCategories)
    }

    private fun getCurrentCategoryIds(productId: Long): Set<Long> {
        return productCategoryRepository.findByProductId(productId)
            .map { it.categoryId }
            .toSet()
    }

    @Transactional(readOnly = true)
    fun getProductDetail(id: Long): ProductDetailResponse {
        val product = productRepository.findById(id)
            .orElseThrow { CustomException(PRODUCT_NOT_FOUND) }

        val productCategories = productCategoryRepository.findByProductId(id)
        if (productCategories.isEmpty()) {
            throw CustomException(PRODUCT_CATEGORY_NOT_MATCHING)
        }
        val categoryIds = productCategories.map { it.categoryId }.toSet()

        return ProductDetailResponse(
            product.id!!,
            product.name,
            product.price,
            product.description,
            product.shortDescription,
            product.stockQuantity,
            product.imageUrl,
            categoryIds
        )
    }
}
