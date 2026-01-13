package com.example.commerce.order.service

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.ORDER_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.ORDER_PRODUCT_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.ORDER_USER_NOT_MATCHING
import com.example.commerce.common.exception.ErrorCode.PRODUCT_MISMATCH_IN_ORDER
import com.example.commerce.common.exception.ErrorCode.PRODUCT_NOT_FOUND
import com.example.commerce.common.exception.ErrorCode.USER_NOT_FOUND
import com.example.commerce.order.domain.NewOrder
import com.example.commerce.order.domain.Order
import com.example.commerce.order.domain.OrderItem
import com.example.commerce.order.domain.OrderState
import com.example.commerce.order.domain.OrderSummary
import com.example.commerce.order.dto.response.OrderItemResponse
import com.example.commerce.order.dto.response.OrderResponse
import com.example.commerce.order.repository.OrderItemRepository
import com.example.commerce.order.repository.OrderRepository
import com.example.commerce.order.support.OrderKeyGenerator
import com.example.commerce.product.repository.ProductRepository
import com.example.commerce.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val orderKeyGenerator: OrderKeyGenerator,
) {

    @Transactional
    fun create(userId: Long, newOrder: NewOrder): String {
        val user = userRepository.findById(userId).orElseThrow { CustomException(USER_NOT_FOUND) }

        val orderProductIds = newOrder.items.map { it.productId }.toSet()
        val productMap = productRepository.findByIdIn(orderProductIds).associateBy { it.id } // product의 id값을 key로 map을 만든다
        if (productMap.isEmpty()) throw CustomException(PRODUCT_NOT_FOUND)
        if (productMap.keys != orderProductIds) throw CustomException(PRODUCT_MISMATCH_IN_ORDER)

        val order = Order(
            user = user,
            key = orderKeyGenerator.generate(),
            name = newOrder.items.first().let { productMap[it.productId]!!.name + if (newOrder.items.size > 1) " 외 ${newOrder.items.size - 1}개" else "" },
            totalPrice = newOrder.items.sumOf { productMap[it.productId]!!.price.multiply(it.quantity.toBigDecimal()) },
            state = OrderState.CREATED,
        )

        val savedOrder = orderRepository.save(order)

        orderItemRepository.saveAll(
            newOrder.items.map {
                val product = productMap[it.productId]!!
                OrderItem(
                    productName = product.name, thumbnailUrl = product.imageUrl, shortDescription = product.shortDescription, quantity = it.quantity, unitPrice = product.price, totalPrice = product.price.multiply(it.quantity.toBigDecimal()), order = savedOrder, product = product
                )
            },
        )

        return savedOrder.key
    }

    @Transactional(readOnly = true)
    fun getOrders(userId: Long): List<OrderSummary> {
        val user = userRepository.findById(userId).orElseThrow { CustomException(USER_NOT_FOUND) }

        val orders = orderRepository.findByUserIdAndStateOrderByIdDesc(userId, OrderState.PAID)
        if (orders.isEmpty()) return emptyList()

        return orders.map {
            OrderSummary(
                id = it.id!!, key = it.key, name = it.name, userId = user.id!!, totalPrice = it.totalPrice, state = it.state
            )
        }
    }

    @Transactional(readOnly = true)
    fun getOrder(
        userId: Long,
        orderKey: String,
        orderState: OrderState,
    ): OrderResponse {
        val user = userRepository.findById(userId).orElseThrow { CustomException(USER_NOT_FOUND) }

        val order = orderRepository.findByKeyAndState(orderKey, orderState).orElseThrow { CustomException(ORDER_NOT_FOUND) }

        if (user.id != order.user.id) {
            throw CustomException(ORDER_USER_NOT_MATCHING)
        }

        val orderItems = orderItemRepository.findByOrderId(order.id!!)
        if (orderItems.isEmpty()) throw CustomException(ORDER_PRODUCT_NOT_FOUND)

        return OrderResponse(
            id = order.id!!, key = order.key, name = order.name, userId = user.id!!, totalPrice = order.totalPrice, state = order.state, items = orderItems.map {
                OrderItemResponse(
                    productId = it.product.id!!, productName = it.productName, thumbnailUrl = it.thumbnailUrl, shortDescription = it.shortDescription, quantity = it.quantity, unitPrice = it.unitPrice, totalPrice = it.totalPrice
                )
            })
    }
}
