//package ru.panov.dao.impl.jdbc;
//
//import org.junit.jupiter.api.*;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.panov.dao.UserDAO;
//import ru.panov.model.Role;
//import ru.panov.model.User;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@Testcontainers
//class JdbcUserDAOImplTest extends AbstractTestcontainers{
//    private static UserDAO userDAO;
//    private Connection connection;
//    @BeforeEach
//    void setUp() throws SQLException {
//        connection = getConnection();
//        connection.setAutoCommit(false);
//        userDAO = new JdbcUserDAOImpl(connection);
//    }
//    @AfterEach
//    void tearDown() throws SQLException {
//        connection.rollback();
//        connection.setAutoCommit(true);
//        connection.close();
//    }
//
//    @Test
//    @DisplayName("Поиск пользователя по id, пользователь найден")
//    void findById_ShouldReturnUserIfExists() {
//        Optional<User> user = userDAO.findById(1L);
//        assertThat(user).isPresent()
//                .get()
//                .hasFieldOrPropertyWithValue("username", "admin");
//    }
//
//    @Test
//    @DisplayName("Поиск пользователя по id, пользователь не найден")
//    void findById_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
//        Optional<User> user = userDAO.findById(999L);
//        assertThat(user).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Получение всех пользователей")
//    void findAll_ShouldReturnAllUsers() {
//        List<User> users = userDAO.findAll();
//
//        assertThat(users).isNotNull();
//        assertThat(users.size()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("Сохранение пользователя, пользователь сохранен")
//    void save_ShouldSaveUser() {
//        User user = User.builder()
//                .username("user2")
//                .password("user2")
//                .build();
//
//        User savedUser = userDAO.save(user);
//
//        assertThat(savedUser).isNotNull()
//                .hasNoNullFieldsOrProperties()
//                .hasFieldOrPropertyWithValue("username", user.getUsername())
//                .hasFieldOrPropertyWithValue("password", user.getPassword())
//                .hasFieldOrPropertyWithValue("role", Role.USER);
//    }
//
//    @Test
//    @DisplayName("Поиск по имени пользователя, пользователь найден")
//    void findByUsername_ShouldReturnUserIfExists() {
//        String username = "admin";
//
//        Optional<User> byUsername = userDAO.findByUsername(username);
//
//        assertThat(byUsername).isPresent()
//                .get()
//                .hasFieldOrPropertyWithValue("username", username);
//    }
//
//    @Test
//    @DisplayName("Поиск по имени пользователя, пользователь не найден")
//    void findByUsername_ShouldReturnEmptyOptionalIfUserDoesNotExist() {
//        String username = "not_found";
//
//        Optional<User> byUsername = userDAO.findByUsername(username);
//
//        assertThat(byUsername).isEmpty();
//    }
//}