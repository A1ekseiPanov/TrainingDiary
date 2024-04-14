package ru.panov.model;

/**
 * Перечисление, представляющее типы аудита.
 */
public enum AuditType {
    /**
     * Успешное событие аудита.
     */
    SUCCESS,
    /**
     * Неудачное событие аудита.
     */
    FAIL
}