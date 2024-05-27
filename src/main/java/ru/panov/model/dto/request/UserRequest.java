package ru.panov.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.panov.model.User;

import java.io.Serializable;

/**
 * DTO для {@link User}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest implements Serializable {
    @NotBlank(message = "username не может быть пустыми или состоять только из пробелов.")
    private String username;
    @NotBlank(message = "password не может быть пустыми или состоять только из пробелов.")
    @Size(min=5,max=30, message = "Длина password должна составлять от 5 до 30 символов.")
    private String password;
}