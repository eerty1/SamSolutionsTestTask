package com.samsolutions.exception_handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String SPLIT_FIELD_NAME_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException methodArgumentNotValidException,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return new ResponseEntity<>(
                adaptFieldName(getFieldError(methodArgumentNotValidException).getField()) + " " +
                        getFieldError(methodArgumentNotValidException).getDefaultMessage(),
                headers,
                status
        );
    }

    private FieldError getFieldError(MethodArgumentNotValidException methodArgumentNotValidException) {
        return methodArgumentNotValidException.getBindingResult().getFieldError();
    }

    private String adaptFieldName(String fieldName) {
        return String.join(" ", fieldName.split(SPLIT_FIELD_NAME_REGEX)).toLowerCase();
    }
}