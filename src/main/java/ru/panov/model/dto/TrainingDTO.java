package ru.panov.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TrainingDTO {
    Long typeId;
    Double countCalories;
    String timeTraining;
    String additionalInformation;
}
