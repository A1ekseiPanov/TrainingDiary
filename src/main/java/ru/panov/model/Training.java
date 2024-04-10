package ru.panov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training {
    private Long id;
    private TrainingType type;
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updated = LocalDateTime.now();
    private Double countCalories;
    private LocalTime timeTraining;
    private String additionalInformation;
    private Long userId;
}
