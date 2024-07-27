package com.samsolutions.exception_handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class EmptyQueryResultExceptionsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handelEmptyQueryResultExceptions(EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity<>(entityNotFoundException.getMessage(), NOT_FOUND);
    }
}
