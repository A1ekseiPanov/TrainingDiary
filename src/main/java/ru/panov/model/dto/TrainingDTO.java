package ru.panov.model.dto;

import lombok.*;
import ru.panov.model.Training;

import java.io.Serializable;

/**
 * DTO для {@link Training}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainingDTO implements Serializable {
    private Long typeId;
    private Double countCalories;
    private String timeTraining;
    private String additionalInformation;
}