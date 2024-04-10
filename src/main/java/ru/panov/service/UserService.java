package ru.panov.service;

import ru.panov.model.User;
import ru.panov.model.dto.UserDTO;

public interface UserService {
    User register(UserDTO userDTO);
    void login(UserDTO userDTO);
    void logout();
    User getLoggedUser();
}