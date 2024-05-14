package ru.panov.model.dto.request;

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
    private String username;
    private String password;
}