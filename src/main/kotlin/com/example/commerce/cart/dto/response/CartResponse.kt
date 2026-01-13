package com.example.commerce.cart.dto.response

import com.example.commerce.cart.domain.CartItem
import java.math.BigDecimal

data class CartResponse(
    val userId: Long,
    val items: List<CartItemResponse>
)

data class CartItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val price: BigDecimal,
    val quantity: Int,
) {

    companion object {

        fun of(cartItem: CartItem): CartItemResponse = CartItemResponse(
            id = cartItem.id!!,
            productId = cartItem.product.id!!,
            productName = cartItem.product.name,
            thumbnailUrl = cartItem.product.imageUrl,
            description = cartItem.product.description,
            shortDescription = cartItem.product.shortDescription,
            price = cartItem.product.price,
            quantity = cartItem.quantity
        )
    }
}
