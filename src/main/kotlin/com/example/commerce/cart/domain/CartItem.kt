package com.example.commerce.cart.domain

import com.example.commerce.common.BaseEntity
import com.example.commerce.product.domain.Product
import com.example.commerce.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "cart_item")
class CartItem(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

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
