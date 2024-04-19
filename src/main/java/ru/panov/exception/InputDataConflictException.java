package ru.panov.exception;

/**
 * Исключение, выбрасываемое при конфликте ввода данных.
 */
public class InputDataConflictException extends RuntimeException {
    /**
     * Конструктор класса с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public InputDataConflictException(String message) {
        super(message);
    }
}