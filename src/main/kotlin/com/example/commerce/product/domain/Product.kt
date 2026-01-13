package com.example.commerce.product.domain

import java.math.BigDecimal

class Product(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val price: BigDecimal,
) {
}
