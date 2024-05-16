package ru.panov.dao.impl.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.panov.config.TestConfig;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.model.TrainingType;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestConfig.class})
@Transactional
class JdbcTrainingTypeDAOImplTest  {
    @Autowired
    private TrainingTypeDAO typeDAO;


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