package com.example.commerce.product.domain

import com.example.commerce.common.BaseEntity
import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.INVALID_PRODUCT_DESCRIPTION
import com.example.commerce.common.exception.ErrorCode.INVALID_PRODUCT_NAME
import com.example.commerce.common.exception.ErrorCode.INVALID_PRODUCT_PRICE
import com.example.commerce.common.exception.ErrorCode.INVALID_PRODUCT_SHORT_DESCRIPTION
import com.example.commerce.common.exception.ErrorCode.INVALID_PRODUCT_STOCK_QUANTITY
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product")
class ProductEntity(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "price", nullable = false)
    var price: BigDecimal,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "short_description", nullable = false)
    var shortDescription: String,

    @Column(name = "thumbnail_url", nullable = false)
    var thumbnailUrl: String,

    @Column(name = "stock_quantity", nullable = false)
    var stockQuantity: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

    init {
        validateName(name)
        validatePrice(price)
        validateDescription(description)
        validateShortDescription(shortDescription)
        validateStockQuantity(stockQuantity)
    }

    fun update(
        name: String?,
        price: BigDecimal?,
        description: String?,
        shortDescription: String?,
        stockQuantity: Int?
    ) {
        // name 이 null 이면 let 블록이 실행되지 않는다.
        name?.let { validateName(it); this.name = it }

        price?.let { validatePrice(it); this.price = it }

        description?.let { validateDescription(it); this.description = it }

        shortDescription?.let { validateShortDescription(it); this.shortDescription = it }

        stockQuantity?.let { validateStockQuantity(it); this.stockQuantity = it }
    }

    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw CustomException(INVALID_PRODUCT_NAME)
        }
    }

    private fun validatePrice(price: BigDecimal) {
        if (price <= BigDecimal.ZERO) {
            throw CustomException(INVALID_PRODUCT_PRICE)
        }
    }

    private fun validateDescription(description: String) {
        if (description.isBlank()) {
            throw CustomException(INVALID_PRODUCT_DESCRIPTION)
        }
    }

    private fun validateShortDescription(shortDescription: String) {
        if (shortDescription.isBlank()) {
            throw CustomException(INVALID_PRODUCT_SHORT_DESCRIPTION)
        }
    }

    private fun validateStockQuantity(stockQuantity: Int) {
        if (stockQuantity < 0) {
            throw CustomException(INVALID_PRODUCT_STOCK_QUANTITY)
        }
    }
}
