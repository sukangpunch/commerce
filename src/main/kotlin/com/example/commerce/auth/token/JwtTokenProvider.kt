package com.example.commerce.auth.token

import com.example.commerce.auth.service.TokenProvider
import org.springframework.stereotype.Component

@Component
class JwtTokenProvider : TokenProvider {

    override fun generateToken(): String {
        return "accessToken"
    }
}
