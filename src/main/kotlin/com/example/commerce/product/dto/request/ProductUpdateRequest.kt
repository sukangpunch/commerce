package com.example.commerce.product.dto.request

import java.math.BigDecimal

data class ProductUpdateRequest(
    val name: String?,
    val description: String?,
    val shortDescription: String?,
    val price: BigDecimal?,
    val stockQuantity: Int?
)
