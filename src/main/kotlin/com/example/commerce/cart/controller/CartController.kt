package com.example.commerce.cart.controller

import com.example.commerce.cart.dto.request.AddCartItemRequest
import com.example.commerce.cart.dto.request.ModifyCartItemRequest
import com.example.commerce.cart.dto.response.CartResponse
import com.example.commerce.cart.service.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/cart")
class CartController(
    private val cartService: CartService
){
    @GetMapping("{user-id}")
    fun getCart(@PathVariable("user-id") userId : Long): ResponseEntity<CartResponse> {
        val response = cartService.getCart(userId)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }

    @PostMapping("/items/{user-id}")
    fun addCartItem(
        @PathVariable("user-id") userId: Long,
        @RequestBody request: AddCartItemRequest
    ): ResponseEntity<Void> {
        cartService.addCartItem(userId, request)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).build()
    }

    @PutMapping("/items/{cart-item-id}")
    fun modifyCartItem(
        @PathVariable("cart-item-id") cartItemId: Long,
        @RequestBody request: ModifyCartItemRequest,
    ): ResponseEntity<Void> {
        cartService.modifyCartItem(cartItemId, request)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).build()
    }

    @DeleteMapping("/items/{cart-item-id}")
    fun deleteCartItem(
        @PathVariable("cart-item-id") cartItemId: Long
    ): ResponseEntity<Void>{
        cartService.deleteCartItem(cartItemId)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).build()
    }
}