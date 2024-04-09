package ru.panov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();
    private String password;
    @Builder.Default
    private Role role = Role.USER;
}