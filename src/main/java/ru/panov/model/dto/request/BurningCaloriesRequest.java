package ru.panov.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BurningCaloriesRequest {
    @Schema(example = "дд.ММ.гггг чч:мм")
    private String dateTimeStart;
    @Schema(example = "дд.ММ.гггг чч:мм")
    private String dateTimeEnd;
}
