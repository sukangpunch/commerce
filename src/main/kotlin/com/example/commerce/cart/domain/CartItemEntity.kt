package com.example.commerce.cart.domain

import com.example.commerce.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "cart_item")
class CartItemEntity(
    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "product_id")
    val productId: Long,

    quantity: Int,  // private 으로 필드 접근을 막지 못하는 이유는 jpa 가 필드를 읽을 수 없기 때문

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

    @Column(name = "quantity", nullable = false)
    var quantity: Int = quantity
        protected set

    fun applyQuantity(value: Int) {
        this.quantity = if (value < 1) 1 else value
    }
}
