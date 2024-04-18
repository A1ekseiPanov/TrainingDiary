package ru.panov.exception;

public class DaoException extends RuntimeException{
    public DaoException(Throwable throwable) {
        super(throwable);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}