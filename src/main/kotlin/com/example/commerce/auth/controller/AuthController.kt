package com.example.commerce.auth.controller

import com.example.commerce.auth.dto.request.SignInRequest
import com.example.commerce.auth.dto.request.SignUpRequest
import com.example.commerce.auth.dto.response.SignInResponse
import com.example.commerce.auth.dto.response.SignUpResponse
import com.example.commerce.auth.service.SignInService
import com.example.commerce.auth.service.SignUpService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val signUpService: SignUpService,
    private val signInService: SignInService,
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse> {
        val response = signUpService.signUp(
            request.name,
            request.email,
            request.password
        )
        val status = HttpStatus.CREATED

        return ResponseEntity.status(status).body(response)
    }

    @PostMapping("/login")
    fun signIn(@RequestBody request: SignInRequest): ResponseEntity<SignInResponse>{
        val response = signInService.signIn(
            request.email,
            request.password
        )
        val status = HttpStatus.OK

        return ResponseEntity.status(status).body(response)
    }
}