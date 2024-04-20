package ru.panov.dao.impl.jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.model.TrainingType;
import ru.panov.util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class JdbcTrainingTypeDAOImplTest {
    private static TrainingTypeDAO typeDAO;
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
        typeDAO = new JdbcTrainingTypeDAOImpl(connection);
        LiquibaseUtil.update(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        postgres.stop();
        connection.close();
    }
    @Test
    @DisplayName("Получение типа тренировки по его id, тип тренировки найден")
    void findById_ExistingIdReturnsTrainingType() {
        Long id = 1L;

        Optional<TrainingType> audit = typeDAO.findById(id);

        assertThat(audit).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("type", "Кардио");
    }

    @Test
    @DisplayName("Получение всех типов тренировок, типы тренировок есть")
    void findAll_ReturnsAllTrainingTypes() {
        List<TrainingType> trainingTypes = typeDAO.findAll();

        assertThat(trainingTypes).isNotEmpty();
        assertThat(trainingTypes.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Сохранение новой тип тренировки, тип тренировки сохранен")
    void save_NewTrainingTypeReturnsSavedTrainingType() {
        TrainingType type = TrainingType.builder()
                .type("Бокс")
                .build();

        TrainingType savedTrainingType = typeDAO.save(type);

        assertThat(savedTrainingType)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("type", type.getType());
    }

    @Test
    @DisplayName("Получение типа тренировки по его названию, тип тренировки найден")
    void findByType_ExistingTypeReturnsTrainingType() {
        String type = "Кардио";

        Optional<TrainingType> audit = typeDAO.findByType(type);

        assertThat(audit).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("type", type);
    }
}