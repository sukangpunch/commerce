package com.example.commerce.product.dto.request

import java.math.BigDecimal

data class ProductCreateRequest(
    val name: String,
    val price: BigDecimal,
    val description: String,
    val shortDescription: String,
    val stockQuantity: Int
)
