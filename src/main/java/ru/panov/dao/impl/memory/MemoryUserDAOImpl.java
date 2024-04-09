package ru.panov.dao.impl.memory;

import ru.panov.dao.UserDAO;
import ru.panov.model.User;

import java.util.*;

import static ru.panov.util.AutoIncrementUtil.increment;

public class MemoryUserDAOImpl implements UserDAO {
    private final Map<Long, User> users = Collections.synchronizedMap(new HashMap<>());

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