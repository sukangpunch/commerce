package com.example.commerce.payment.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kakaopay")
data class KakaoPayProperties(
    val secretKey: String,
    val cid: String,
)
