package com.example.commerce.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: Int,
    val message: String
) {

    // NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "상품이 존재하지 않습니다"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "카테고리를 찾을 수 없습니다"),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "카트 아이템을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "주문을 찾을 수 없습니다."),
    ORDER_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "주문의 상품을 찾을 수 없습니다."),

    // User
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),

    // Product
    INVALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 이름입니다"),
    INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 가격입니다."),
    INVALID_PRODUCT_STOCK_QUANTITY(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 재고 수량입니다."),
    INVALID_PRODUCT_DESCRIPTION(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 설명입니다."),
    INVALID_PRODUCT_SHORT_DESCRIPTION(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요약 설명입니다."),
    INVALID_CATEGORY_NAME(HttpStatus.BAD_REQUEST.value(), "유효하지 않는 카테고리 이름입니다"),
    CATEGORY_REQUIRED(HttpStatus.BAD_REQUEST.value(), "카테고리는 최소 1개 이상 선택해야 합니다"),
    PRODUCT_CATEGORY_NOT_MATCHING(HttpStatus.BAD_REQUEST.value(), "상품에 카테고리가 매핑되어 있지 않습니다."),
    CATEGORY_NAME_DUPLICATED(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 카테고리 이름입니다"),

    // Order
    ORDER_PRODUCT_QUANTITY_INVALID(HttpStatus.BAD_REQUEST.value(), "주문 시 상품 수량은 최소 1개 이상이어야 합니다."),
    PRODUCT_MISMATCH_IN_ORDER(HttpStatus.BAD_REQUEST.value(), "주문의 상품과 조회한 상품이 매칭되지 않습니다."),
    ORDER_USER_NOT_MATCHING(HttpStatus.BAD_REQUEST.value(), "주문을 조회할 권한이 존재하지 않습니다."),

    // Database
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT.value(), "데이터베이스 무결성 제약조건 위반이 발생했습니다."),

    // General
    JSON_PARSING_FAILED(HttpStatus.BAD_REQUEST.value(), "JSON 파싱을 할 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "값을 입력할 수 없습니다."),
    NOT_DEFINED_ERROR(HttpStatus.BAD_REQUEST.value(), "에러가 발생했습니다."),
    ;
}
