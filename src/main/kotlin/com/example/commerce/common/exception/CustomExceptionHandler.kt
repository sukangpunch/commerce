package com.example.commerce.common.exception

import com.example.commerce.common.exception.ErrorCode.*
import com.example.commerce.common.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    companion object {
        private val log = LoggerFactory.getLogger(CustomException::class.java)
    }

    @ExceptionHandler(CustomException::class)
    protected fun handleCustomException(ex: CustomException): ResponseEntity<ErrorResponse> {
        log.error("커스텀 예외 발생 : {}", ex.message)
        return ResponseEntity
            .status(ex.code)
            .body(ErrorResponse(ex))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorMessage = ex.bindingResult.fieldErrors
            .mapNotNull { it.defaultMessage }
            .toString()

        log.error("입력값 검증 예외 발생 : {}", errorMessage)

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(ErrorResponse(INVALID_INPUT, errorMessage))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        log.error("데이터 무결성 제약조건 위반 예외 발생 : {}", ex.message)
        val errorMessage = "데이터 무결성 제약조건 위반 예외 발생"

        return ResponseEntity
            .status(DATA_INTEGRITY_VIOLATION.code)
            .body(ErrorResponse(DATA_INTEGRITY_VIOLATION, errorMessage))
    }

    @ExceptionHandler(Exception::class)
    fun handleOtherException(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("서버 내부 예외 발생 : {}", ex.message)

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(NOT_DEFINED_ERROR, ex.message))
    }
}