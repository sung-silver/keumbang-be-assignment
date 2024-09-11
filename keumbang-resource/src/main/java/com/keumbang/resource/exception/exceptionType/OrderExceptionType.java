package com.keumbang.resource.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderExceptionType implements ExceptionType {
  // 400
  INVALID_ORDER_TYPE(HttpStatus.BAD_REQUEST, "주문 타입이 올바르지 않습니다"),
  INVALID_UPDATE_ORDER(HttpStatus.BAD_REQUEST, "올바르지 않은 주문 상태 변경 요청입니다"),
  INVALID_DELETE_ORDER(HttpStatus.BAD_REQUEST, "올바르지 않은 주문 삭제 요청입니다");
  ;
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
