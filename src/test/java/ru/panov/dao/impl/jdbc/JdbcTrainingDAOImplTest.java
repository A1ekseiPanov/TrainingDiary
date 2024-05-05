//package ru.panov.dao.impl.jdbc;
//
//import org.junit.jupiter.api.*;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.panov.dao.TrainingDAO;
//import ru.panov.model.Training;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Testcontainers
//class JdbcTrainingDAOImplTest  extends AbstractTestcontainers{
//    private static TrainingDAO trainingDAO;
//    private Connection connection;
//    @BeforeEach
//    void setUp() throws SQLException {
//        connection = getConnection();
//        connection.setAutoCommit(false);
//        trainingDAO = new JdbcTrainingDAOImpl(connection);
//    }
//    @AfterEach
//    void tearDown() throws SQLException {
//        connection.rollback();
//        connection.setAutoCommit(true);
//        connection.close();
//    }
//    @Test
//    @DisplayName("Сохранение новой тренировки, тренировка сохранена")
//    void save_NewTrainingReturnsSavedTrainingWithId() {
//        Long userId = 2L;
//        Long typeId = 2L;
//        Training training = Training.builder()
//                .trainingTime(LocalTime.of(12, 32))
//                .countCalories(310d)
//                .additionalInfo("присед 4 подхода по 20 раз")
//                .typeId(typeId)
//                .userId(userId)
//                .build();
//
//        Training savedTraining = trainingDAO.save(training, userId);
//
//        assertThat(savedTraining)
//                .isNotNull()
//                .hasNoNullFieldsOrProperties()
//                .hasFieldOrPropertyWithValue("trainingTime", training.getTrainingTime())
//                .hasFieldOrPropertyWithValue("countCalories", training.getCountCalories())
//                .hasFieldOrPropertyWithValue("additionalInfo", training.getAdditionalInfo());
//    }
//
//    @Test
//    @DisplayName("Получение тренировки по её id, тренераовка найдена")
//    void findById_ExistingIdReturnsTraining() {
//        Long userId = 2L;
//        Long trainingId = 1L;
//
//        Optional<Training> trainingOptional = trainingDAO.findById(trainingId, userId);
//        assertThat(trainingOptional).isPresent()
//                .get()
//                .hasFieldOrPropertyWithValue("trainingTime", LocalTime.of(1, 0))
//                .hasFieldOrPropertyWithValue("countCalories", 302d)
//                .hasFieldOrPropertyWithValue("additionalInfo", "4км");
//    }
//
//    @Test
//    @DisplayName("Получение тренировки по её id, тренировка не найдена")
//    void findById_NonExistingIdReturnsEmptyOptional() {
//        Long userId = 9999L;
//        Long trainingId = 1000L;
//
//        Optional<Training> trainingOptional = trainingDAO.findById(trainingId, userId);
//        assertThat(trainingOptional).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Получение всех тренировок")
//    void findAll_ReturnsAllTraining() {
//        List<Training> trainingList = trainingDAO.findAll();
//
//        assertThat(trainingList).isNotEmpty();
//        assertThat(trainingList.size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Удаление, тренировка найдена и удалена")
//    void delete_ExistingIdReturnsTrue() {
//        Long userId = 2L;
//        Long trainingId = 1L;
//
//        boolean delete = trainingDAO.delete(trainingId, userId);
//
//        assertThat(delete).isTrue();
//        assertThat(trainingDAO.findAll()).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Удаление, тренировка не найдена")
//    void delete_NonExistingIdReturnsFalse() {
//        Long userId = 12345L;
//        Long trainingId = 12345L;
//
//        boolean delete = trainingDAO.delete(trainingId, userId);
//
//        assertThat(delete).isFalse();
//        assertThat(trainingDAO.findAll().size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Расход калорий за период, расход за период есть, возвращает правильное количество калорий")
//    void caloriesSpentOverPeriod_ReturnsCorrectCalories() {
//        Long userId = 2L;
//
//        Double caloriesSpent = trainingDAO.caloriesSpentOverPeriod(
//                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1), userId);
//        assertThat(caloriesSpent).isEqualTo(302.0);
//    }
//
//    @Test
//    @DisplayName("Расход калорий за период, расхода за период нет, возвращает значение по умолчанию")
//    void caloriesSpentOverPeriod_ReturnsDefaultValue() {
//        Long userId = 2L;
//
//        Double caloriesSpent = trainingDAO.caloriesSpentOverPeriod(
//                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), userId);
//
//        assertThat(caloriesSpent).isEqualTo(0.0);
//    }
//}