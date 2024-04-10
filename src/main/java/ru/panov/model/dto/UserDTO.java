package ru.panov.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDTO {
    private String username;

    private String password;
}
