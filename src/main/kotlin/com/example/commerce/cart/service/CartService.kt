package com.example.commerce.cart.service

import com.example.commerce.cart.domain.AddCartItem
import com.example.commerce.cart.domain.Cart
import com.example.commerce.cart.domain.CartItem
import com.example.commerce.cart.domain.CartItemEntity
import com.example.commerce.cart.domain.ModifyCartItem
import com.example.commerce.cart.repository.CartItemRepository
import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.CART_ITEM_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.PRODUCT_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.USER_NOT_FOUND
import com.example.commerce.product.domain.Product
import com.example.commerce.product.domain.ProductEntity
import com.example.commerce.product.repository.ProductRepository
import com.example.commerce.user.domain.UserEntity
import com.example.commerce.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun getCart(userId: Long): Cart {
        val user: UserEntity = userRepository.findById(userId)
            .orElseThrow { CustomException(USER_NOT_FOUND) }
        val items = cartItemRepository.findByUserId(user.id!!)
        val productMap = productRepository.findAllById(items.map { it.productId })
            .associateBy { it.id } // productId 를 key 로 하고, product 객체를  value 로 하는 map 으로 리턴

        return Cart(
            userId = user.id!!,
            items = items.filter { productMap.containsKey(it.productId) } // 상품이 존재하는 장바구니 아이템만 선택(productMap 에 없는 상품 제외)
                .map {
                    CartItem(
                        id = it.id!!,
                        product = Product(
                            id = productMap[it.productId]!!.id!!,
                            name = productMap[it.productId]!!.name,
                            thumbnailUrl = productMap[it.productId]!!.thumbnailUrl,
                            description = productMap[it.productId]!!.description,
                            shortDescription = productMap[it.productId]!!.shortDescription,
                            price = productMap[it.productId]!!.price,
                        ),
                        quantity = it.quantity
                    )
                }
        )
    }

    @Transactional
    fun addCartItem(userId: Long, item: AddCartItem): Long {
        val user: UserEntity = userRepository.findById(userId)
            .orElseThrow { CustomException(USER_NOT_FOUND) }

        val product: ProductEntity = productRepository.findById(item.productId)
            .orElseThrow { CustomException(PRODUCT_NOT_FOUND) }

        return cartItemRepository.findByUserIdAndProductId(user.id!!, product.id!!)
            ?.apply {
                applyQuantity(quantity)
            }?.id
            ?: cartItemRepository.save(
                CartItemEntity(
                    userId = user.id!!,
                    productId = product.id!!,
                    quantity = item.quantity,
                )
            ).id!!
    }

    @Transactional
    fun modifyCartItem(cartItemId: Long, item: ModifyCartItem) {
        val cartItem: CartItemEntity = cartItemRepository.findById(cartItemId)
            .orElseThrow { CustomException(CART_ITEM_NOT_FOUND) }

        cartItem.applyQuantity(item.quantity)
    }

    @Transactional
    fun deleteCartItem(cartItemId: Long) {
        val cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow { CustomException(CART_ITEM_NOT_FOUND) }
        cartItemRepository.delete(cartItem)
        // deleteById 는 select 쿼리 + delete 쿼리 둘 다 발생(즉 2번 쿼리 발생)
        // 추후 cartItem 조회 + 토큰에서 가져온 userId를 활용하여 cartItem 의 유효를 검증
    }
}
