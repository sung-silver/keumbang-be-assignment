package com.keumbang.resource.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderSuccessType implements SuccessType {
  CREATE_ORDER_SUCCESS(HttpStatus.CREATED, "주문 생성에 성공하였습니다."),
  UPDATE_ORDER_STATUS_SUCCESS(HttpStatus.OK, "주문 상태 변경에 성공하였습니다."),
  READ_ORDER_SUCCESS(HttpStatus.OK, "주문 상세 조회에 성공하였습니다.");

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
