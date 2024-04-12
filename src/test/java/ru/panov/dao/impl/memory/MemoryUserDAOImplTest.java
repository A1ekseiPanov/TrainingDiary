package ru.panov.dao.impl.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.panov.dao.UserDAO;
import ru.panov.model.Role;
import ru.panov.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryUserDAOImplTest {
    private UserDAO memoryUserDAO;
    private final User user1 = User.builder()
            .username("user1")
            .password("user1")
            .build();
    private final User user2 = User.builder()
            .username("user2")
            .password("user2")
            .build();


    @BeforeEach
    void init() {
        memoryUserDAO = new MemoryUserDAOImpl();
        memoryUserDAO.save(user1);
        memoryUserDAO.save(user2);
    }


    @Test
    void findAll_ShouldReturnAllUsers() {
        List<User> users = memoryUserDAO.findAll();
        assertThat(users.size()).isEqualTo(3);
        assertThat(users).contains(user1, user2);
    }

    @Test
    void save_ShouldSaveUserAndIncrementId() {
        memoryUserDAO.save(user1);
        User user = memoryUserDAO.save(user2);
        List<User> users = memoryUserDAO.findAll();
        assertThat(users.size()).isEqualTo(5);
        assertThat(user.getId()).isEqualTo(5);
        assertThat(user.getRole()).isEqualTo(Role.USER);

    }

    @Test
    void findById_ShouldReturnUserIfExists() {
        Optional<User> user = memoryUserDAO.findById(2L);
        assertThat(user).isPresent();
        assertThat(user.get()).isEqualTo(user1);

    }

    @Test
    void findById_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
        Optional<User> user = memoryUserDAO.findById(999L);
        assertThat(user).isEmpty();
    }

    @Test
    void findByUsername_ShouldReturnUserIfExists() {
        Optional<User> user = memoryUserDAO.findByUsername(user1.getUsername());
        assertThat(user).isPresent();
        assertThat(user.get()).isEqualTo(user1);
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
        Optional<User> user = memoryUserDAO.findByUsername("not_found");
        assertThat(user).isEmpty();
    }
}