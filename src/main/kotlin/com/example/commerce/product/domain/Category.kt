package com.example.commerce.product.domain

import com.example.commerce.common.BaseEntity
import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.INVALID_CATEGORY_NAME
import jakarta.persistence.*

@Entity
@Table(
    name = "categories",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_category_name",
            columnNames = ["name"]
        )
    ]
)
class Category (
    @Column(name = "name", nullable = false)
    var name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity(){

    init{
        validateName(name)
    }

    private fun validateName(name: String) {
        if(name.isBlank()){
            throw CustomException(INVALID_CATEGORY_NAME)
        }
    }
}