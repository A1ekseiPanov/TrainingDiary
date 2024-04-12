package ru.panov.dao.impl.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.panov.dao.TrainingDAO;
import ru.panov.model.Training;
import ru.panov.model.TrainingType;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryTrainingDAOImplTest {
    private TrainingDAO trainingDAO;
    private final TrainingType trainingType = TrainingType.builder().type("Бег").build();
    private Training training;
    private final Training training1 = Training.builder()
            .timeTraining(LocalTime.of(10, 12))
            .countCalories(300d)
            .additionalInformation("3км")
            .type(trainingType)
            .userId(1L)
            .build();
    private final Training training2 = Training.builder()
            .timeTraining(LocalTime.of(10, 12))
            .countCalories(300d)
            .additionalInformation("3км")
            .type(trainingType)
            .userId(2L)
            .build();
    private final Training training3 = Training.builder()
            .timeTraining(LocalTime.of(11, 32))
            .countCalories(3001d)
            .additionalInformation("10км")
            .type(trainingType)
            .userId(3L)
            .build();

    @BeforeEach
    void init() {
        trainingDAO = new MemoryTrainingDAOImpl();
        training = trainingDAO.save(training1, training1.getUserId());
        trainingDAO.save(training2, training2.getUserId());
    }

    @Test
    void findById_ExistingIdReturnsTrainingType() {
        Optional<Training> trainingOptional = trainingDAO.findById(training.getId(), training.getUserId());
        assertThat(trainingOptional).isPresent();
        assertThat(trainingOptional.get()).isEqualTo(training);
    }

    @Test
    void findById_NonExistingIdReturnsEmptyOptional() {
        Optional<Training> trainingOptional = trainingDAO.findById(1000L, 9999L);
        assertThat(trainingOptional).isEmpty();
    }


    @Test
    void findAll_ReturnsAllTrainingTypes() {
        List<Training> trainingList = trainingDAO.findAll();
        assertThat(trainingList.size()).isEqualTo(2);
        Training tr = trainingDAO.save(training3, training3.getUserId());
        List<Training> trainingList2 = trainingDAO.findAll();
        assertThat(trainingList2.size()).isEqualTo(3);
        assertThat(trainingList2).contains(training, tr);
    }

    @Test
    void save_NewTrainingTypeReturnsSavedTrainingTypeWithId() {
        Training training4 = Training.builder()
                .timeTraining(LocalTime.of(12, 32))
                .countCalories(302d)
                .additionalInformation("4км")
                .type(trainingType)
                .userId(4L)
                .build();
        Training tr = trainingDAO.save(training4, training4.getUserId());
        List<Training> trainings = trainingDAO.findAll();
        assertThat(trainings.size()).isEqualTo(3);
        assertThat(training4).isEqualTo(tr);
        assertThat(tr.getUserId()).isEqualTo(4);
    }

    @Test
    void delete_RemovesTraining() {
        List<Training> trainings = trainingDAO.findAll();
        int size = trainings.size();
        trainingDAO.delete(training.getId(), training.getUserId());
        assertThat(size - 1).isEqualTo(trainingDAO.findAll().size());
        assertThat(trainingDAO.findAll()).doesNotContain(training);
    }

    @Test
    void caloriesSpentOverPeriodReturnsCorrectCalories() {
        Double caloriesSpent = trainingDAO.caloriesSpentOverPeriod(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1), 1L);
        assertThat(caloriesSpent).isEqualTo(300.0);
    }
}