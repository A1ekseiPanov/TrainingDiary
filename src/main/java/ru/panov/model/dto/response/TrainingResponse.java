package ru.panov.model.dto.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainingResponse implements Serializable {
    private Long trainingId;
    private Long typeId;
    private Double countCalories;
    private String timeTraining;
    private String additionalInformation;
    private String dateTraining;
    private Long userId;
}
