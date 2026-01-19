package com.inditex.pricing.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(PriceNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFound(
      PriceNotFoundException ex, HttpServletRequest request) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    pd.setTitle("Price not found");
    pd.setDetail(ex.getMessage());
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("path", request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
  }

  @ExceptionHandler({
    ConstraintViolationException.class,
    MissingServletRequestParameterException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<ProblemDetail> handleBadRequest(Exception ex, HttpServletRequest request) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Invalid request");
    pd.setDetail(ex.getMessage());
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("path", request.getRequestURI());
    return ResponseEntity.badRequest().body(pd);
  }
}
