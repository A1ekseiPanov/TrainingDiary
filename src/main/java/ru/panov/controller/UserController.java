package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import ru.panov.exception.InputDataConflictException;
import ru.panov.exception.NotFoundException;
import ru.panov.model.User;
import ru.panov.model.dto.UserDTO;
import ru.panov.service.UserService;

@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    public void register(UserDTO userDTO) {
        try {
            userService.register(userDTO);
        } catch (InputDataConflictException e) {
            System.out.println(e.getMessage());
        }
    }

    public void login(UserDTO userDTO) {
        try {
            userService.login(userDTO);
        } catch (InputDataConflictException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void logout() {
        try {
            userService.logout();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    public User loggedUser() {
        return userService.getLoggedUser();
    }
}
