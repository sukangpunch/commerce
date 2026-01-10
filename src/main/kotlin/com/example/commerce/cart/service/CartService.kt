package com.example.commerce.cart.service

import com.example.commerce.cart.domain.CartItem
import com.example.commerce.cart.dto.request.AddCartItemRequest
import com.example.commerce.cart.dto.request.ModifyCartItemRequest
import com.example.commerce.cart.dto.response.CartItemResponse
import com.example.commerce.cart.dto.response.CartResponse
import com.example.commerce.cart.repository.CartItemRepository
import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.CART_ITEM_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.USER_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.PRODUCT_NOT_FOUND
import com.example.commerce.product.domain.Product
import com.example.commerce.product.repository.ProductRepository
import com.example.commerce.user.domain.User
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
    fun getCart(userId: Long): CartResponse {
        val user: User =  userRepository.findById(userId)
            .orElseThrow{ CustomException(USER_NOT_FOUND) }
        val items = cartItemRepository.findByUserId(user.id!!)
        val productMap = productRepository.findAllById(items.map { it.product.id })
            .associateBy{ it.id } // productId 를 key 로 하고, product 객체를  value 로 하는 map 으로 리턴

        return CartResponse(
            userId = user.id!!,
            items = items.filter{ productMap.containsKey(it.product.id) } // 상품이 존재하는 장바구니 아이템만 선택(productMap 에 없는 상품 제외)
                .map{ cartItem ->
                    val product = productMap[cartItem.product.id]!!
                    CartItemResponse(
                        id = cartItem.id!!,
                        productId = product.id!!,
                        productName = product.name,
                        thumbnailUrl = product.imageUrl,
                        description = product.description,
                        shortDescription = product.shortDescription,
                        price = product.price,
                        quantity = cartItem.quantity
                    )
                }
        )
    }

    @Transactional
    fun addCartItem(userId: Long, request: AddCartItemRequest): Long {
        val user: User =  userRepository.findById(userId)
            .orElseThrow{ CustomException(USER_NOT_FOUND) }

        val product: Product = productRepository.findById(request.productId)
            .orElseThrow{ CustomException(PRODUCT_NOT_FOUND) }

        val quantity = request.quantity

        return cartItemRepository.findByUserIdAndProductId(user.id!!, product.id!!)
            ?.apply {
                applyQuantity(quantity)
            }?.id
            ?: cartItemRepository.save(
                CartItem(
                    user = user,
                    product = product,
                    quantity = quantity
                )
            ).id!!
    }

    @Transactional
    fun modifyCartItem(cartItemId: Long, request: ModifyCartItemRequest){
        val cartItem: CartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow{ CustomException(CART_ITEM_NOT_FOUND) }

        cartItem.applyQuantity(request.quantity)
    }

    @Transactional
    fun deleteCartItem(cartItemId: Long){
        val cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow{ CustomException(CART_ITEM_NOT_FOUND) }
        cartItemRepository.delete(cartItem)
        // deleteById 는 select 쿼리 + delete 쿼리 둘 다 발생(즉 2번 쿼리 발생)
        // 추후 cartItem 조회 + 토큰에서 가져온 userId를 활용하여 cartItem 의 유효를 검증
    }
}