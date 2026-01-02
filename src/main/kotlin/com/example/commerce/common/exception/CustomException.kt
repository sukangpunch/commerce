package com.example.commerce.common.exception

class CustomException(
    val code: Int,
    override val message: String
) : RuntimeException(message) {

    constructor(
        errorCode: ErrorCode,
        detail: String? = null
    ): this(
        code = errorCode.code,
        message = detail?.let { "${errorCode.message} : $it" } ?: errorCode.message
    )
}

