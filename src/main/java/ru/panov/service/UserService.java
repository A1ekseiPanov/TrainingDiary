package ru.panov.service;

import ru.panov.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(String username, String password);
    Optional<User> authorize(String username, String password);

    User getById(Long id);
    List<User> showAllUsers();
}