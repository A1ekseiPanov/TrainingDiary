package ru.panov.exception;

public class InputDataConflictException extends RuntimeException{
    public InputDataConflictException(String message) {
        super(message);
    }
}