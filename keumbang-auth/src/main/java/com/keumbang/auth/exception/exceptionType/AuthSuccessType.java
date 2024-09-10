package com.keumbang.auth.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthSuccessType implements SuccessType {
  SUCCESS_SIGNUP(HttpStatus.CREATED, "회원가입에 성공하였습니다."),
  SUCCESS_LOGIN(HttpStatus.OK, "로그인에 성공하였습니다."),
  SUCCESS_TOKEN_REFRESH(HttpStatus.OK, "토큰 재발급에 성공하였습니다.");

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
