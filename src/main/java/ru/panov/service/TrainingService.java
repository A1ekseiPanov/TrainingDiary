package ru.panov.service;

import ru.panov.model.Training;
import ru.panov.model.dto.TrainingDTO;

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
    List<Training> findAll(Long userId);

    /**
     * Найти тренировку пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @param id     Идентификатор тренировки.
     * @return Тренировка пользователя с указанным идентификатором.
     */
    Training findById(Long userId, Long id);

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

    /**
     * Рассчитать количество сожженных калорий за период.
     *
     * @param dateTimeStart Начальная дата и время периода.
     * @param dateTimeEnd   Конечная дата и время периода.
     * @param userId        Идентификатор пользователя.
     * @return Количество сожженных калорий за указанный период.
     */
    Double caloriesSpentOverPeriod(String dateTimeStart, String dateTimeEnd, Long userId);

    /**
     * Сохранить новую тренировку пользователя.
     *
     * @param userId    Идентификатор пользователя.
     * @param training  Данные новой тренировки.
     * @return Сохраненная тренировка пользователя.
     */
    Training save(Long userId, TrainingDTO training);
}