package ru.panov.dao.impl.jdbc;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.panov.dao.TrainingDAO;
import ru.panov.model.Training;
import ru.panov.util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcTrainingDAOImplTest {
    private static TrainingDAO trainingDAO;
    private static Connection connection;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.7-alpine");

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgres.start();
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        trainingDAO = new JdbcTrainingDAOImpl(connection);
        LiquibaseUtil.update(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        postgres.stop();
        connection.close();
    }

    @Test
    @Order(1)
    @DisplayName("Сохранение новой тренировки, тренировка сохранена")
    void save_NewTrainingReturnsSavedTrainingWithId() {
        Long userId = 2L;
        Long typeId = 2L;
        Training training = Training.builder()
                .trainingTime(LocalTime.of(12, 32))
                .countCalories(302d)
                .additionalInfo("4км")
                .typeId(typeId)
                .userId(userId)
                .build();

        Training savedTraining = trainingDAO.save(training, userId);

        assertThat(savedTraining)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("trainingTime", training.getTrainingTime())
                .hasFieldOrPropertyWithValue("countCalories", 302d)
                .hasFieldOrPropertyWithValue("additionalInfo", "4км");
    }

    @Test
    @Order(2)
    @DisplayName("Получение тренировки по её id, тренераовка найдена")
    void findById_ExistingIdReturnsTraining() {
        Long userId = 2L;
        Long trainingId = 1L;

        Optional<Training> trainingOptional = trainingDAO.findById(trainingId, userId);
        assertThat(trainingOptional).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("trainingTime", LocalTime.of(12, 32))
                .hasFieldOrPropertyWithValue("countCalories", 302d)
                .hasFieldOrPropertyWithValue("additionalInfo", "4км");
    }

    @Test
    @Order(2)
    @DisplayName("Получение тренировки по её id, тренировка не найдена")
    void findById_NonExistingIdReturnsEmptyOptional() {
        Long userId = 9999L;
        Long trainingId = 1000L;

        Optional<Training> trainingOptional = trainingDAO.findById(trainingId, userId);
        assertThat(trainingOptional).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("Получение всех тренировок")
    void findAll_ReturnsAllTraining() {
        List<Training> trainingList = trainingDAO.findAll();

        assertThat(trainingList).isNotEmpty();
        assertThat(trainingList.size()).isEqualTo(1);
    }

    @Test
    @Order(4)
    @DisplayName("Удаление, тренировка найдена и удалена")
    void delete_ExistingIdReturnsTrue() {
        Long userId = 2L;
        Long trainingId = 1L;

        boolean delete = trainingDAO.delete(trainingId, userId);

        assertThat(delete).isTrue();
        assertThat(trainingDAO.findAll()).isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("Удаление, тренировка не найдена")
    void delete_NonExistingIdReturnsFalse() {
        Long userId = 12345L;
        Long trainingId = 12345L;

        boolean delete = trainingDAO.delete(trainingId, userId);

        assertThat(delete).isFalse();
        assertThat(trainingDAO.findAll().size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    @DisplayName("Расход калорий за период, расход за период есть, возвращает правильное количество калорий")
    void caloriesSpentOverPeriod_ReturnsCorrectCalories() {
        Long userId = 2L;

        Double caloriesSpent = trainingDAO.caloriesSpentOverPeriod(
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1), userId);
        assertThat(caloriesSpent).isEqualTo(302.0);
    }

    @Test
    @Order(2)
    @DisplayName("Расход калорий за период, расхода за период нет, возвращает значение по умолчанию")
    void caloriesSpentOverPeriod_ReturnsDefaultValue() {
        Long userId = 2L;

        Double caloriesSpent = trainingDAO.caloriesSpentOverPeriod(
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), userId);

        assertThat(caloriesSpent).isEqualTo(0.0);
    }
}