package ru.panov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс Audit представляет сущность аудита.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {
    /**
     * Идентификатор аудита.
     */
    private Long id;
    /**
     * Время и дата аудита.
     * По умолчанию устанавливается текущее время.
     */
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();
    /**
     * Имя класса, в котором произошло событие аудита.
     */
    private String className;
    /**
     * Имя метода, в котором произошло событие аудита.
     */
    private String methodName;
    /**
     * Тип аудита.
     */
    private AuditType auditType;
    /**
     * Имя пользователя, совершившего действие.
     */
    private String username;
}
