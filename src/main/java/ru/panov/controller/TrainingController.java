package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import ru.panov.model.Training;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.model.dto.TrainingTypeDTO;
import ru.panov.service.TrainingService;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;

import java.util.List;

/**
 * Контроллер для управления операциями тренировок и типов тренировок.
 */
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;
    private final UserService userService;
    private final TrainingTypeService typeTrainingService;

    /**
     * Получает все тренировки для текущего пользователя.
     *
     * @return Список тренировок.
     */
    public List<Training> getAllTraining() {
        return trainingService.findAll(getIdLoggedUser());
    }

    /**
     * Получает тренировку по её идентификатору для текущего пользователя.
     *
     * @param id Идентификатор тренировки.
     * @return Тренировка.
     */
    public Training getByTrainingById(Long id) {
        return trainingService.findById(getIdLoggedUser(), id);
    }

    /**
     * Удаляет тренировку по её идентификатору для текущего пользователя.
     *
     * @param id Идентификатор тренировки.
     */
    public void deleteTraining(Long id) {
        trainingService.delete(getIdLoggedUser(), id);
    }

    /**
     * Обновляет данные тренировки для текущего пользователя.
     *
     * @param id          Идентификатор тренировки.
     * @param trainingDTO Данные для обновления.
     * @return Обновленная тренировка.
     */
    public Training updateTraining(Long id, TrainingDTO trainingDTO) {
        return trainingService.update(id, trainingDTO, getIdLoggedUser());
    }

    /**
     * Рассчитывает количество сожженных калорий за определенный период для текущего пользователя.
     *
     * @param dateTimeStart Начальная дата и время периода.
     * @param dateTimeEnd   Конечная дата и время периода.
     * @return Количество сожженных калорий.
     */
    public Double caloriesSpentOverPeriod(String dateTimeStart, String dateTimeEnd) {
        return trainingService.caloriesSpentOverPeriod(dateTimeStart, dateTimeEnd, getIdLoggedUser());
    }

    /**
     * Создает новую тренировку для текущего пользователя.
     *
     * @param trainingDTO Данные для создания тренировки.
     * @return Созданная тренировка.
     */
    public Training createTraining(TrainingDTO trainingDTO) {
        return trainingService.save(getIdLoggedUser(), trainingDTO);
    }

    /**
     * Получает все типы тренировок.
     *
     * @return Список типов тренировок.
     */
    public List<TrainingType> getAllTrainingType() {
        return typeTrainingService.findAll();
    }

    /**
     * Создает новый тип тренировки.
     *
     * @param typeDTO Данные для создания типа тренировки.
     * @return Созданный тип тренировки.
     */
    public TrainingType createTypeTraining(TrainingTypeDTO typeDTO) {
        return typeTrainingService.save(typeDTO);
    }

    /**
     * Получает идентификатор текущего пользователя.
     *
     * @return Идентификатор текущего пользователя.
     */
    private Long getIdLoggedUser() {
        return userService.getLoggedUser().getId();
    }
}