package ru.panov.model.dto.request;

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
public class TrainingTypeRequest implements Serializable {
    private String type;
}