package ru.panov.dao.impl.memory;

import ru.panov.dao.UserDAO;
import ru.panov.model.Role;
import ru.panov.model.User;

import java.util.*;

import static ru.panov.util.AutoIncrementUtil.increment;

/**
 * Реализация интерфейса UserDAO для работы с пользователсям в памяти.
 */
public class MemoryUserDAOImpl implements UserDAO {
    /**
     * Хрангилище пользователей.
     */
    private final Map<Long, User> users = Collections.synchronizedMap(new HashMap<>());

    /**
     * Конструктор класса. Создает учетную запись администратора по умолчанию.
     */
    public MemoryUserDAOImpl() {
        save(User.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .build());
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User save(User user) {
        user.setId(increment(users));
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> Objects.equals(username, user.getUsername()))
                .findFirst();
    }
}