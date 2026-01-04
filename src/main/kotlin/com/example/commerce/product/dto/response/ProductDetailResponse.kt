package com.example.commerce.product.dto.response

import java.math.BigDecimal

data class ProductDetailResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val shortDescription: String,
    val stockQuantity: Int,
    val imageUrl: String,
    val categories: Set<Long>
)
