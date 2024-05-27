package ru.panov.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
    @NotNull(message = "typeId не может быть null")
    @Positive(message = "typeId должен быть больше 0")
    private Long typeId;
    @NotNull(message = "countCalories не может быть null")
    @Positive(message = "countCalories должено быть больше 0")
    private Double countCalories;
    @Schema(example = "чч:мм:сс")
    @NotBlank(message = "timeTraining не может быть пустым или состоять только из пробелов")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2}", message = "timeTraining должно соответствовать: чч:мм:сс")
    private String timeTraining;
    private String additionalInformation;
}