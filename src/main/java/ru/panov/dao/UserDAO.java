package ru.panov.dao;

import ru.panov.model.User;

import java.util.Optional;

public interface UserDAO extends AbstractBaseDAO<Long, User> {
    Optional<User> findByUsername(String username);
}