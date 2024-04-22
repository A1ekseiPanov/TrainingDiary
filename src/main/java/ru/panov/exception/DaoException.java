package ru.panov.exception;

/**
 * Исключение, используемое для обработки ошибок в слое доступа к данным (DAO).
 */
public class DaoException extends RuntimeException {
    /**
     * Конструктор с параметром для инициализации исключения.
     *
     * @param throwable Передаваемое исключение.
     */
    public DaoException(Throwable throwable) {
        super(throwable);
    }
}