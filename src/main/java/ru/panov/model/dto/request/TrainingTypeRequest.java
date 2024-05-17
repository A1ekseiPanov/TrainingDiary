package ru.panov.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "type не может быть пустым или состоять только из пробелов")
    @Size(min = 2, message = "Длина type должна составлять минимум от 2 символов")
    private String type;
}