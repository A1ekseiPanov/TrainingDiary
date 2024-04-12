package ru.panov.model.dto;

import lombok.Builder;
import lombok.Value;
import ru.panov.model.TrainingType;

/**
 * DTO для {@link TrainingType}.
 */
@Value
@Builder
public class TrainingTypeDTO {
    String type;
}
