package com.example.commerce.auth.service

import com.example.commerce.auth.dto.response.SignUpResponse
import com.example.commerce.user.domain.Role
import com.example.commerce.user.domain.UserEntity
import com.example.commerce.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SignUpService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun signUp(name: String, email: String, password: String): SignUpResponse {
        val user = UserEntity(
            name,
            email,
            password,
            Role.USER
        )
        userRepository.save(user)

        return SignUpResponse(user.name, user.email);
    }
}
