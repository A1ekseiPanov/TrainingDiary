package ru.panov.dao;

import ru.panov.model.User;

import java.util.Optional;

/**
 * Интерфейс доступа к данным для сущности пользователя.
 */
public interface UserDAO extends AbstractBaseDAO<Long, User> {
    /**
     * Поиск пользователя по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Optional, содержащий найденного пользователя, если такой существует, иначе пустой Optional.
     */
    Optional<User> findByUsername(String username);
}