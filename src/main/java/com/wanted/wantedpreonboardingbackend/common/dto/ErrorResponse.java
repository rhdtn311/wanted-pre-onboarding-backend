package com.wanted.wantedpreonboardingbackend.common.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;

@Getter
public class ErrorResponse {

    private static final String INVALID_INPUT_CODE = "INVALID_INPUT_ERROR";
    private static final String INVALID_INPUT_MESSAGE = "입력값 검증 오류입니다.";

    private final String code;
    private final String message;
    private final List<InputError> inputErrors;

    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.inputErrors = null;
    }

    private ErrorResponse(String code, String message, List<InputError> inputErrors) {
        this.code = code;
        this.message = message;
        this.inputErrors = inputErrors;
    }

    public static ErrorResponse basic(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.name(), message);
    }

    public static ErrorResponse input(List<FieldError> filedErrors) {
        List<InputError> inputErrors = getInputErrors(filedErrors);

        return new ErrorResponse(INVALID_INPUT_CODE, INVALID_INPUT_MESSAGE, inputErrors);
    }

    public static ErrorResponse input(Set<ConstraintViolation<?>> constraintViolations) {
        List<InputError> inputErrors = getInputErrors(constraintViolations);

        return new ErrorResponse(INVALID_INPUT_CODE, INVALID_INPUT_MESSAGE, inputErrors);
    }

    public static ErrorResponse input(String message, String invalidParameter) {
        return new ErrorResponse(INVALID_INPUT_CODE, INVALID_INPUT_MESSAGE, getInputErrors(message, invalidParameter));
    }

    private static List<InputError> getInputErrors(List<FieldError> filedErrors) {
        return filedErrors.stream()
                .map(fieldError -> new InputError(fieldError.getDefaultMessage(), fieldError.getField()))
                .toList();
    }

    private static List<InputError> getInputErrors(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(constraintViolation -> new InputError(constraintViolation.getMessage(), getFieldByPath(constraintViolation.getPropertyPath())))
                .toList();
    }

    private static List<InputError> getInputErrors(String message, String invalidParameter) {
        return List.of(new InputError(message, invalidParameter));
    }

    private static String getFieldByPath(Path path) {
        return path.toString()
                .substring(path.toString().lastIndexOf(".") + 1);
    }

    @Getter
    @AllArgsConstructor
    static class InputError {
        private final String message;
        private final String field;
    }
}