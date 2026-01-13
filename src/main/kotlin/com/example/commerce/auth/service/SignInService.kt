package com.example.commerce.auth.service

import com.example.commerce.auth.dto.response.SignInResponse
import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode.PASSWORD_MISMATCH
import com.example.commerce.common.exception.ErrorCode.USER_NOT_FOUND
import com.example.commerce.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class SignInService(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider
) {

    fun signIn(email: String, password: String): SignInResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { CustomException(USER_NOT_FOUND) }

        if (user.password != password) {
            throw CustomException(PASSWORD_MISMATCH)
        }

        val token = tokenProvider.generateToken();
        return SignInResponse(token)
    }
}
