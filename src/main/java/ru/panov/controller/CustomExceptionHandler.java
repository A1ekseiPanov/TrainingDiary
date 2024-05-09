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

import java.time.format.DateTimeParseException;

/**
 * Обработчик исключений для контроллеров.
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Обрабатывает исключение NotFoundException.
     *
     * @param ex исключение NotFoundException
     * @return ответ с деталями ошибки и статусом NOT_FOUND
     */
    @ExceptionHandler(value
            = {NotFoundException.class})
    protected ResponseEntity<ProblemDetail> notFoundError(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Обрабатывает исключение InputDataConflictException.
     *
     * @param ex исключение InputDataConflictException
     * @return ответ с деталями ошибки и статусом CONFLICT
     */
    @ExceptionHandler(value
            = {InputDataConflictException.class})
    protected ResponseEntity<ProblemDetail> InputDataError(InputDataConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage()));
    }

    /**
     * Обрабатывает исключение IllegalArgumentException.
     *
     * @param ex исключение IllegalArgumentException
     * @return ответ с деталями ошибки и статусом FORBIDDEN
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<ProblemDetail> IllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    /**
     * Обрабатывает исключение ValidationException.
     *
     * @param ex исключение ValidationException
     * @return ответ с деталями ошибки и статусом FORBIDDEN
     */
    @ExceptionHandler(value
            = {ValidationException.class})
    protected ResponseEntity<ProblemDetail> validationError(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    /**
     * Обрабатывает исключение DateTimeParseException.
     *
     * @param ex исключение DateTimeParseException
     * @return ответ с сообщением о неверном вводе времени и статусом FORBIDDEN
     */
    @ExceptionHandler(value
            = {DateTimeParseException.class})
    protected ResponseEntity<ProblemDetail> validationError(DateTimeParseException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Неверный ввод времени, даты"));
    }

    /**
     * Обрабатывает общее исключение.
     *
     * @param ex общее исключение
     * @return ответ с деталями ошибки и статусом INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ProblemDetail> handleExpiredJwtException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}