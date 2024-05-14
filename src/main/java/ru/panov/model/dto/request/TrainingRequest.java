package ru.panov.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class TrainingRequest implements Serializable {
    private Long typeId;
    private Double countCalories;
    @Schema(example = "чч:мм:сс")
    private String timeTraining;
    private String additionalInformation;
}