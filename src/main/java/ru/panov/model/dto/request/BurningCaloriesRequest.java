package ru.panov.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BurningCaloriesRequest {
    @Schema(example = "дд.ММ.гггг чч:мм")
    @NotBlank(message = "dateTimeStart не может быть пустым или состоять только из пробелов")
    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}", message = "dateTimeStart дожно соответствовать условию: дд.ММ.гггг чч:мм. Пример: 01.01.2000 10:10")
    private String dateTimeStart;
    @Schema(example = "дд.ММ.гггг чч:мм")
    @NotBlank(message = "dateTimeEnd не может быть пустым или состоять только из пробелов")
    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}", message = "dateTimeEnd дожно соответствовать условию: дд.ММ.гггг чч:мм. Пример: 01.01.2000 10:11")
    private String dateTimeEnd;
}