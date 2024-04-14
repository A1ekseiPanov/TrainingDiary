package ru.panov.exception;

/**
 * Исключение, выбрасываемое при валидации данных.
 */
public class ValidationException extends RuntimeException {
    /**
     * Конструктор класса с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public ValidationException(String message) {
        super(message);
    }
}