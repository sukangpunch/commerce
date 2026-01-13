package com.example.commerce.product.domain

import com.example.commerce.common.BaseEntity
import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.INVALID_CATEGORY_NAME
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "category",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_category_name",
            columnNames = ["name"]
        )
    ]
)
class CategoryEntity(
    @Column(name = "name", nullable = false)
    var name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

    init {
        validateName(name)
    }

    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw CustomException(INVALID_CATEGORY_NAME)
        }
    }
}
