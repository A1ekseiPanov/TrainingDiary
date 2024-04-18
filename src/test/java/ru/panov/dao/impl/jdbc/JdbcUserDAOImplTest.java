package ru.panov.dao.impl.jdbc;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.panov.dao.UserDAO;
import ru.panov.model.Role;
import ru.panov.model.User;
import ru.panov.util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcUserDAOImplTest {
    private static UserDAO userDAO;
    private static Connection connection;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.7-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgres.start();
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        userDAO = new JdbcUserDAOImpl(connection);
        LiquibaseUtil.update(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        postgres.stop();
        connection.close();
    }

    @Test
    void findById_ShouldReturnUserIfExists() {
        Optional<User> user = userDAO.findById(1L);
        assertThat(user).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "admin");

    }

    @Test
    void findById_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
        Optional<User> user = userDAO.findById(999L);
        assertThat(user).isEmpty();
    }

    @Test
    @Order(1)
    void findAll_ShouldReturnAllUsers() {
        List<User> users = userDAO.findAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void save_ShouldSaveUser() {
        User user = User.builder()
                .username("user2")
                .password("user2")
                .build();

        User savedUser = userDAO.save(user);

        assertThat(savedUser).isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("username", user.getUsername())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("role", Role.USER);
    }

    @Test
    void findByUsername_ShouldReturnUserIfExists() {
        String username = "admin";

        Optional<User> byUsername = userDAO.findByUsername(username);

        assertThat(byUsername).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", username);
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
        String username = "not_found";

        Optional<User> byUsername = userDAO.findByUsername(username);

        assertThat(byUsername).isEmpty();
    }
}