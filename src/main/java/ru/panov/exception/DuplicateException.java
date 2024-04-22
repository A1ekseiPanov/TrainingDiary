package ru.panov.exception;

/**
 * Исключение, выбрасываемое при попытке создать дубликат сущности.
 */
public class DuplicateException extends RuntimeException {
    /**
     * Конструктор класса с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public DuplicateException(String message) {
        super(message);
    }
}