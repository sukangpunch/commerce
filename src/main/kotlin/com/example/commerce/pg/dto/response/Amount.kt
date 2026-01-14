package com.example.commerce.pg.dto.response

class Amount(
    val total: Int,
    val tax_free: Int,
    val vat: Int,
    val point: Int,
    val discount: Int,
    val green_deposit: Int,
) {
}
