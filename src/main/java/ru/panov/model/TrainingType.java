package ru.panov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс TrainingType представляет сущность типа тренировки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingType {
    /**
     * Идентификатор типа тренировки.
     */
    private Long id;
    /**
     * Название типа тренировки.
     */
    private String type;
    /**
     * Время создания типа тренировки.
     * По умолчанию устанавливается текущее время.
     */
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
}