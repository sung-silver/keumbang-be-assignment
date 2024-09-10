package com.keumbang.resource.exception;

import static com.keumbang.resource.exception.exceptionType.CommonExceptionType.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.keumbang.resource.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
  private static final int FIRST_INDEX = 0;

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    return ResponseEntity.status(ex.getExceptionType().status())
        .body(ErrorResponse.of(ex.getExceptionType()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleServerException(RuntimeException ex) {
    log.error("🚨 InternalException occurred: {} 🚨", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NoHandlerFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            ErrorResponse.of(
                NOT_FOUND_PATH, NOT_FOUND_PATH.getMessage() + ": " + ex.getRequestURL()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    StringBuilder builder = new StringBuilder();

    if (bindingResult.hasErrors()) {
      FieldError fieldError = bindingResult.getFieldErrors().get(FIRST_INDEX);
      builder
          .append("[")
          .append(fieldError.getField())
          .append("](은)는 ")
          .append(fieldError.getDefaultMessage())
          .append(" 입력된 값: [")
          .append(fieldError.getRejectedValue())
          .append("]");
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ErrorResponse.of(
                INVALID_INPUT_VALUE, INVALID_INPUT_VALUE.getMessage() + ": " + builder));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ErrorResponse.of(
                INVALID_REQUEST_PARAM_TYPE,
                INVALID_REQUEST_PARAM_TYPE.getMessage()
                    + ": "
                    + ex.getParameter().getParameterName()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ErrorResponse.of(
                NOT_NULL_REQUEST_PARAM,
                NOT_NULL_REQUEST_PARAM.getMessage() + ": " + ex.getParameterName()));
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException() {
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .body(ErrorResponse.of(INVALID_JSON_TYPE, INVALID_JSON_TYPE.getMessage()));
  }
}
