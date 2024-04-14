package ru.panov.model.dto;

import lombok.Builder;
import lombok.Value;
import ru.panov.model.User;

/**
 * DTO для {@link User}.
 */
@Value
@Builder
public class UserDTO {
    String username;
    String password;
}
