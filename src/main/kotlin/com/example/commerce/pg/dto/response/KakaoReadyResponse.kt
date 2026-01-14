package com.example.commerce.pg.dto.response

data class KakaoReadyResponse(
    val tid: String,
    val next_redirect_app_url: String,
    val next_redirect_mobile_url: String,
    val next_redirect_pc_url: String,
    val android_app_scheme: String,
    val ios_app_scheme: String,
    val created_at: String,
)
