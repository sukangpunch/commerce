package com.example.commerce.auth.service

interface TokenProvider {

    fun generateToken(): String
}
