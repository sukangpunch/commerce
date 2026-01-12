package com.example.commerce.order.support

import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.util.*

@Component
class OrderKeyGenerator {
    fun generate(): String{
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
            ByteBuffer.allocate(16).apply {
                UUID.randomUUID().also {
                    putLong(it.mostSignificantBits)
                    putLong(it.leastSignificantBits)
                }
            }.array(),
        )
    }
}
