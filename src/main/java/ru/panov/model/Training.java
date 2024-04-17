package ru.panov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Класс Training представляет сущность тренировки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training {
    /**
     * Идентификатор тренировки.
     */
    private Long id;
    /**
     * Идентификатор типа тренировки.
     */
    private Long typeId;
    /**
     * Время создания тренировки.
     * По умолчанию устанавливается текущее время.
     */
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
    /**
     * Время последнего обновления тренировки.
     * По умолчанию устанавливается текущее время.
     */
    @Builder.Default
    private LocalDateTime updated = LocalDateTime.now();
    /**
     * Количество сожженных калорий.
     */
    private Double countCalories;
    /**
     * Время тренировки.
     */
    private LocalTime trainingTime;
    /**
     * Дополнительная информация о тренировке.
     */
    private String additionalInfo;
    /**
     * Идентификатор пользователя, к которому относится тренировка.
     */
    private Long userId;
}
