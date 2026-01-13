package com.example.commerce.product.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "product_categories",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_product_category",
            columnNames = ["product_id", "category_id"]
        )
    ],
    indexes = [   // FK 는 Index 를 자동으로 추가하지 않는다.
        Index(name = "idx_product_id", columnList = "product_id"),
        Index(name = "idx_category_id", columnList = "category_id")
    ]
)
class ProductCategory(
    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "category_id", nullable = false)
    val categoryId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
}
