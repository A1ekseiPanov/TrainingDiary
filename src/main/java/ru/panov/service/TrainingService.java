package ru.panov.service;

import ru.panov.model.Training;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.model.dto.request.BurningCaloriesRequest;
import ru.panov.model.dto.response.TrainingResponse;

import java.util.List;

/**
 * Интерфейс сервиса для работы с тренировками.
 */
public interface TrainingService {
    /**
     * Получить список всех тренировок пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список всех тренировок пользователя.
     */
    List<TrainingResponse> findAll(Long userId);

    /**
     * Найти тренировку пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @param id     Идентификатор тренировки.
     * @return Тренировка пользователя с указанным идентификатором.
     */
    TrainingResponse findById(Long userId, Long id);

    /**
     * Удалить тренировку пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param id     Идентификатор тренировки.
     */
    void delete(Long userId, Long id);

    /**
     * Обновить информацию о тренировке пользователя.
     *
     * @param id       Идентификатор тренировки.
     * @param training Новые данные тренировки.
     * @param userId   Идентификатор пользователя.
     * @return Обновленная тренировка пользователя.
     */
    Training update(Long id, TrainingDTO training, Long userId);


    Double caloriesSpentOverPeriod(BurningCaloriesRequest burningCaloriesRequest, Long userId);

    /**
     * Сохранить новую тренировку пользователя.
     *
     * @param userId   Идентификатор пользователя.
     * @param training Данные новой тренировки.
     * @return Сохраненная тренировка пользователя.
     */
    Training save(Long userId, TrainingDTO training);
}