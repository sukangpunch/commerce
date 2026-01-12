package com.example.commerce.order.domain

import com.example.commerce.common.BaseEntity
import com.example.commerce.product.domain.Product
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "order_item")
class OrderItem(
    @Column(name = "product_name", nullable = false)
    val productName: String,

    @Column(name = "thumbnail_url", nullable = false)
    val thumbnailUrl: String,

    @Column(name = "short_description", nullable = false)
    val shortDescription: String,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Column(name = "unit_price", nullable = false)
    val unitPrice: BigDecimal,

    @Column(name = "total_price", nullable = false)
    val totalPrice: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) : BaseEntity(){
}
