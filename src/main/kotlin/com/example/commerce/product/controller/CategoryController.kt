package com.example.commerce.product.controller

import com.example.commerce.product.dto.request.CategoryCreateRequest
import com.example.commerce.product.dto.response.CategoryResponse
import com.example.commerce.product.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/category"])
class CategoryController(
    private val categoryService: CategoryService
) {
    @PostMapping("/create")
    fun createCategory(@RequestBody request: CategoryCreateRequest): ResponseEntity<CategoryResponse> {
        val response = categoryService.addCategory(request)
        val status = HttpStatus.CREATED
        return ResponseEntity.status(status).body(response)
    }

    @GetMapping("/all")
    fun getAllCategory(): ResponseEntity<List<CategoryResponse>> {
        val response = categoryService.findAllCategory()
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }

    @GetMapping("{product-id}")
    fun getCategoriesByProduct(@PathVariable("product-id") productId: Long): ResponseEntity<List<CategoryResponse>> {
        val response = categoryService.findCategoriesByProduct(productId)
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }
}