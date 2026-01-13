package com.example.commerce.auth.service

import org.springframework.stereotype.Component

@Component
class AuthTokenProvider(
    private val tokenProvider: TokenProvider
) {

    fun generateAccessToken(): String {
        return tokenProvider.generateToken()
    }
}
