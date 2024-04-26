package ru.panov.model.dto.response;

import lombok.*;
import ru.panov.model.TrainingType;

import java.io.Serializable;

/**
 * DTO для {@link TrainingType}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainingTypeResponse implements Serializable {
    private Long id;
    private String type;
}