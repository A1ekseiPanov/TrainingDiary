package ru.panov.in.view;

import lombok.RequiredArgsConstructor;
import ru.panov.controller.UserController;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.ValidationException;
import ru.panov.model.Role;
import ru.panov.model.dto.UserDTO;
import ru.panov.in.view.factory.ViewFactory;

import java.util.Scanner;

@RequiredArgsConstructor
public class AuthView {
    private final UserController userController;

    /**
     * Регистрирует нового пользователя.
     *
     * @param scanner объект Scanner для ввода пользователя
     */
    public void registerUser(Scanner scanner) {
        System.out.println("Регистрация:");
        System.out.print("Введите username: ");
        String username = scanner.next();
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        try {
            userController.register(UserDTO.builder()
                    .username(username)
                    .password(password)
                    .build());
        } catch (InputDataConflictException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Выполняет вход в систему.
     *
     * @param scanner объект Scanner для ввода пользователя
     */
    public void login(Scanner scanner) {
        System.out.println("Вход:");
        System.out.print("Введите username: ");
        String username = scanner.next();
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        userController.login(UserDTO.builder()
                .username(username)
                .password(password)
                .build());
        if (userController.loggedUser() != null) {
            if (userController.loggedUser().getRole().equals(Role.USER)) {
                ViewFactory.getInstance().getUserView().runUserMenu(scanner);
            } else {
                ViewFactory.getInstance().getAdminView().runAdminMenu(scanner);
            }
        } else {
            System.out.println("Войдите в систему");
        }
    }
}