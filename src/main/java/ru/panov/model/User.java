package ru.panov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс User представляет сущность пользователя.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * Идентификатор пользователя.
     */
    private Long id;
    /**
     * Имя пользователя.
     */
    private String username;
    /**
     * Дата регистрации пользователя.
     * По умолчанию устанавливается текущая дата и время.
     */
    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Роль пользователя.
     * По умолчанию устанавливается роль "USER".
     */
    @Builder.Default
    private Role role = Role.USER;
}