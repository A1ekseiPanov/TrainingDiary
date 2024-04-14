package ru.panov.exception;

/**
 * Исключение, выбрасываемое при неудачном поиске сущности.
 */
public class NotFoundException extends RuntimeException {
    /**
     * Конструктор класса с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public NotFoundException(String message) {
        super(message);
    }
}