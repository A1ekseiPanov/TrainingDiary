package ru.panov.model.dto;

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
public class UserDTO implements Serializable {
    private String username;
    private String password;
}