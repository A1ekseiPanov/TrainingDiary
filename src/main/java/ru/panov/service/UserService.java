package ru.panov.service;

import ru.panov.model.User;
import ru.panov.model.dto.response.JwtTokenResponse;
import ru.panov.model.dto.request.UserRequest;
import ru.panov.model.dto.response.UserResponse;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface UserService {
    /**
     * Регистрация нового пользователя.
     *
     * @param userRequest Данные нового пользователя.
     * @return Зарегистрированный пользователь.
     */
    UserResponse register(UserRequest userRequest);

    /**
     * Вход пользователя в систему.
     *
     * @param userRequest Данные пользователя для входа.
     */
    JwtTokenResponse login(UserRequest userRequest);

    /**
     *Получение пользователя по его имени.
     *
     * @param username Имя пользователя.
     * @return Пользователь.
     */
    User getByUsername(String username);

    /**
     *Получение пользователя по его id.
     *
     * @param id Id пользователя.
     * @return Пользователь.
     */
    User getById(Long id);
}