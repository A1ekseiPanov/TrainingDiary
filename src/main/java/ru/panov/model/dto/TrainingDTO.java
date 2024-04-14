package ru.panov.model.dto;

import lombok.Builder;
import lombok.Value;
import ru.panov.model.Training;

/**
 * DTO для {@link Training}.
 */
@Value
@Builder
public class TrainingDTO {
    Long typeId;
    Double countCalories;
    String timeTraining;
    String additionalInformation;
}
