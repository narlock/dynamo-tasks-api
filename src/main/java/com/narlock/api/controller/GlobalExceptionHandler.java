package com.narlock.api.controller;

import com.narlock.api.model.exception.ItemNotFoundException;
import com.narlock.api.model.response.ErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final String BAD_REQUEST = "Bad Request";
  private static final String NOT_FOUND = "Not Found";
  private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

  @ExceptionHandler(ItemNotFoundException.class)
  public ResponseEntity<Object> handleItemNotFoundException(
      ItemNotFoundException ex, WebRequest request) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(NOT_FOUND)
            .message(ex.getMessage())
            .path(request.getDescription(false))
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(BAD_REQUEST)
            .message(ex.getMessage())
            .path(request.getDescription(false))
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(INTERNAL_SERVER_ERROR)
            .message(ex.getMessage())
            .path(request.getDescription(false))
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
