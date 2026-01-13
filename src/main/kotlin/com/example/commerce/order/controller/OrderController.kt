package com.example.commerce.order.controller

import com.example.commerce.cart.service.CartService
import com.example.commerce.order.domain.OrderState
import com.example.commerce.order.dto.request.CreateOrderFromCartRequest
import com.example.commerce.order.dto.request.CreateOrderRequest
import com.example.commerce.order.dto.response.CreateOrderResponse
import com.example.commerce.order.dto.response.OrderListResponse
import com.example.commerce.order.dto.response.OrderResponse
import com.example.commerce.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/order")
class OrderController(
    private val orderService: OrderService,
    private val cartService: CartService
) {

    @PostMapping("/create")
    fun createOrder(
        @RequestParam("userId") userId: Long,
        @RequestBody request: CreateOrderRequest
    ): ResponseEntity<CreateOrderResponse> {
        val response = orderService.create(
            userId = userId,
            newOrder = request.toNewOrder(userId),
        )
        val status = HttpStatus.CREATED
        return ResponseEntity.status(status).body(
            CreateOrderResponse(
                orderKey = response
            )
        )
    }

    @PostMapping("/cart-orders/create")
    fun createFromCart(
        @RequestParam("userId") userId: Long,
        @RequestBody request: CreateOrderFromCartRequest
    ): ResponseEntity<CreateOrderResponse> {
        val cart = cartService.getCart(userId)
        val response = orderService.create(
            userId = userId,
            newOrder = cart.toNewOrder(request.cartItemIds)
        )
        val status = HttpStatus.CREATED
        return ResponseEntity.status(status).body(
            CreateOrderResponse(
                orderKey = response,
            )
        )
    }

    @GetMapping("/catalog")
    fun getOrders(@RequestParam("userId") userId: Long, ): ResponseEntity<List<OrderListResponse>> {
        val response = orderService.getOrders(userId)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(
            OrderListResponse.of(
                orders = response
            )
        )
    }

    @GetMapping("/{order-key}")
    fun getOrder(
        @RequestParam("userId") userId: Long,
        @PathVariable("order-key") orderKey: String
    ): ResponseEntity<OrderResponse> {
        val response = orderService.getOrder(
            userId = userId,
            orderKey = orderKey,
            orderState = OrderState.PAID
        )
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(OrderResponse.of(response))
    }

    // 상품 체크(포인트, 쿠폰 등 적용은 나중에)
}
