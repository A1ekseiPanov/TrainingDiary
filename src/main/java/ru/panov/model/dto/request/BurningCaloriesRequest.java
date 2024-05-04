package ru.panov.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BurningCaloriesRequest {
    private String dateTimeStart;
    private String dateTimeEnd;
}
