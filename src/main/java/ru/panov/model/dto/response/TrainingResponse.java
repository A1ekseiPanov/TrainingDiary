package ru.panov.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainingResponse implements Serializable {
    private Long trainingId;
    private Long typeId;
    private Double countCalories;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime timeTraining;
    private String additionalInformation;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateTraining;
    private Long userId;
}
