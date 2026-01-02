package com.example.commerce.common.response

import com.example.commerce.common.exception.CustomException
import com.example.commerce.common.exception.ErrorCode

data class ErrorResponse(
    val message: String
) {
    constructor(e: CustomException): this(
        e.message ?: "알 수 없는 오류가 발생했습니다"
    )

    constructor(errorCode: ErrorCode, detail: String? = null) : this(
        detail?.let { "${errorCode.message} : $it" } ?: errorCode.message
    )
}