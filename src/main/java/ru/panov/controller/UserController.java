package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.model.User;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.UserService;

/**
 * Контроллер для управления операциями пользователей.
 */
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param userDTO Данные нового пользователя.
     */
    public void register(UserDTO userDTO) {
        try {
            userService.register(userDTO);
        } catch (InputDataConflictException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Аутентифицирует пользователя.
     *
     * @param userDTO Данные пользователя для аутентификации.
     */
    public void login(UserDTO userDTO) {
        try {
            userService.login(userDTO);
        } catch (InputDataConflictException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Выполняет выход текущего пользователя.
     */
    public void logout() {
        try {
            userService.logout();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Получает данные о текущем авторизованном пользователе.
     *
     * @return Данные текущего пользователя.
     */
    public User loggedUser() {
        return userService.getLoggedUser();
    }
}