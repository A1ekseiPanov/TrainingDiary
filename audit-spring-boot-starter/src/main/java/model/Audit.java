package model;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime created = LocalDateTime.now();
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
    private AuditType type;
    /**
     * Имя пользователя, совершившего действие.
     */
    private String username;
}