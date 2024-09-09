package com.keumbang.auth.exception;

import com.keumbang.auth.exception.exceptionType.ExceptionType;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ExceptionType exceptionType;

  public CustomException(ExceptionType exceptionType) {
    super(exceptionType.message());
    this.exceptionType = exceptionType;
  }
}
