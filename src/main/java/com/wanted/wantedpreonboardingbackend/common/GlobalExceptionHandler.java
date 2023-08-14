package com.wanted.wantedpreonboardingbackend.common;

import com.wanted.wantedpreonboardingbackend.common.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.input(exception.getFieldErrors()));
    }

    @ExceptionHandler(value = BindException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(BindException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.input(exception.getFieldErrors()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.input(exception.getConstraintViolations()));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.input(exception.getErrorCode(), exception.getParameter().getParameterName()));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.input(exception.getMessage(), exception.getParameterName()));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.basic(METHOD_NOT_ALLOWED, exception.getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.basic(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.basic(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }
}