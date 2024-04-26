package ru.panov.service;

import ru.panov.model.dto.request.TrainingTypeRequest;
import ru.panov.model.dto.response.TrainingTypeResponse;

import java.util.List;

/**
 * Интерфейс сервиса для работы с типами тренировок.
 */
public interface TrainingTypeService {
    /**
     * Найти тип тренировки по идентификатору.
     *
     * @param id Идентификатор типа тренировки.
     * @return Тип тренировки с указанным идентификатором.
     */
    TrainingTypeResponse findById(Long id);

    /**
     * Получить список всех типов тренировок.
     *
     * @return Список всех типов тренировок.
     */
    List<TrainingTypeResponse> findAll();

    /**
     * Сохранить новый тип тренировки.
     *
     * @param type Данные нового типа тренировки.
     * @return Сохраненный тип тренировки.
     */
    TrainingTypeResponse save(TrainingTypeRequest type);
}