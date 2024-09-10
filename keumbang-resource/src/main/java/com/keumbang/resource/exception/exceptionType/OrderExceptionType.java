package com.keumbang.resource.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderExceptionType implements ExceptionType {
  // 400
  INVALID_ORDER_TYPE(HttpStatus.NOT_FOUND, "주문 타입이 올바르지 않습니다");

  private final HttpStatus status;
  private final String message;

  @Override
  public HttpStatus status() {
    return this.status;
  }

  @Override
  public String message() {
    return this.message;
  }
}
