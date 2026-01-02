package com.example.commerce.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: Int,
    val message: String
) {
    // NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다"),

    // User
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),

    // Database
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT.value(), "데이터베이스 무결성 제약조건 위반이 발생했습니다."),

    // General
    JSON_PARSING_FAILED(HttpStatus.BAD_REQUEST.value(), "JSON 파싱을 할 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "값을 입력할 수 없습니다."),
    NOT_DEFINED_ERROR(HttpStatus.BAD_REQUEST.value(), "에러가 발생했습니다."),
    ;

}