package com.keumbang.auth.exception.exceptionType;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
  HttpStatus status();

  String message();
}
