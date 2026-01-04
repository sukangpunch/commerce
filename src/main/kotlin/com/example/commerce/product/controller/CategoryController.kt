package com.example.commerce.product.controller

import com.example.commerce.product.dto.response.CategoryResponse
import com.example.commerce.product.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/categories"])
class CategoryController(
    private val categoryService: CategoryService
) {
    @PostMapping("/create")
    fun createCategory(@RequestBody category: CategoryResponse): ResponseEntity<CategoryResponse> {
        val response = categoryService.addCategory(category.name)
        val status = HttpStatus.CREATED
        return ResponseEntity.status(status).body(response)
    }

    @GetMapping("/all")
    fun getAllCategory(): ResponseEntity<List<CategoryResponse>> {
        val response = categoryService.findAllCategory()
        val status = HttpStatus.OK
        return ResponseEntity.status(status).body(response)
    }
}