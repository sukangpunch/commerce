package com.example.commerce.auth.dto.request

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)
