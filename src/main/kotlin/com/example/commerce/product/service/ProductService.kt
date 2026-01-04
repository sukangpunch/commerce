package com.example.commerce.product.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.PRODUCT_NOT_FOUND
import com.example.commerce.product.domain.Product
import com.example.commerce.product.dto.response.ProductDetailResponse
import com.example.commerce.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    @Transactional
    fun createProduct(
        name : String,
        price : BigDecimal,
        description : String,
        shortDescription : String,
        stockQuantity : Int
    ): ProductDetailResponse {
        val imageUrl = "http://test-image.png"
        val product = Product(name, price, description, imageUrl, shortDescription, stockQuantity)
        productRepository.save(product)

        return ProductDetailResponse(
            product.id!!,
            product.name,
            product.price,
            product.description,
            product.shortDescription,
            product.stockQuantity,
            product.imageUrl
        )
    }

    @Transactional(readOnly = true)
    fun getProductDetail(id: Long): ProductDetailResponse {
        val product = productRepository.findById(id)
            .orElseThrow{ CustomException(PRODUCT_NOT_FOUND) }

        return ProductDetailResponse(
            product.id!!,
            product.name,
            product.price,
            product.description,
            product.shortDescription,
            product.stockQuantity,
            product.imageUrl
        )
    }

    @Transactional
    fun updateProduct(
        id: Long,
        name: String?,
        price: BigDecimal?,
        description: String?,
        shortDescription: String?,
        stockQuantity: Int?
    ): ProductDetailResponse {
        val product = productRepository.findById(id)
            .orElseThrow{ CustomException(PRODUCT_NOT_FOUND) }

        product.update(
            name,
            price,
            description,
            shortDescription,
            stockQuantity
        )

        return ProductDetailResponse(
            product.id!!,
            product.name,
            product.price,
            product.description,
            product.shortDescription,
            product.stockQuantity,
            product.imageUrl
        )
    }
}