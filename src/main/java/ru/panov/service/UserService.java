package ru.panov.service;

import ru.panov.model.User;
import ru.panov.model.dto.JwtTokenResponse;
import ru.panov.model.dto.UserDTO;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface UserService {
    /**
     * Регистрация нового пользователя.
     *
     * @param userDTO Данные нового пользователя.
     * @return Зарегистрированный пользователь.
     */
    User register(UserDTO userDTO);

    /**
     * Вход пользователя в систему.
     *
     * @param userDTO Данные пользователя для входа.
     */
    JwtTokenResponse login(UserDTO userDTO);

    User getByUsername(String username);

    User getById(Long id);
}