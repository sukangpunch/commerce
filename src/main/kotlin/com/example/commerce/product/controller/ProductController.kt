package com.example.commerce.product.controller

import com.example.commerce.product.dto.request.ProductCreateRequest
import com.example.commerce.product.dto.request.ProductUpdateRequest
import com.example.commerce.product.dto.response.ProductDetailResponse
import com.example.commerce.product.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping("/create")
    fun createProduct(@RequestBody request: ProductCreateRequest): ResponseEntity<ProductDetailResponse> {
        val response = productService.createProduct(request)
        val status = HttpStatus.CREATED
        return ResponseEntity.status(status).body(response)
    }

    @GetMapping("/{product-id}")
    fun getProductDetail(@PathVariable("product-id") id: Long): ResponseEntity<ProductDetailResponse> {
        val response = productService.getProductDetail(id)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }

    @PatchMapping("/update/{product-id}")
    fun updateProductInfo(
        @PathVariable("product-id") id: Long,
        @RequestBody request: ProductUpdateRequest
    ): ResponseEntity<ProductDetailResponse> {
        val response = productService.updateProduct(id, request)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }
}
