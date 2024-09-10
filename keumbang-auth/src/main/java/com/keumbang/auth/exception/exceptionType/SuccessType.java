package com.keumbang.auth.exception.exceptionType;

import org.springframework.http.HttpStatus;

public interface SuccessType {
  HttpStatus status();

  String message();

  default int getHttpStatusCode() {
    return status().value();
  }
}
