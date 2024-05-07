package ru.panov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = {NotFoundException.class})
    protected ResponseEntity<ProblemDetail> notFoundError(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(value
            = {InputDataConflictException.class})
    protected ResponseEntity<ProblemDetail> InputDataError(InputDataConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<ProblemDetail> IllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(value
            = {ValidationException.class})
    protected ResponseEntity<ProblemDetail> validationError(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ProblemDetail> handleExpiredJwtException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}