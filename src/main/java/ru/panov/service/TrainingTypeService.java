package ru.panov.service;

import ru.panov.model.TrainingType;
import ru.panov.model.dto.TrainingTypeDTO;

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
    TrainingType findById(Long id);

    /**
     * Получить список всех типов тренировок.
     *
     * @return Список всех типов тренировок.
     */
    List<TrainingType> findAll();

    /**
     * Сохранить новый тип тренировки.
     *
     * @param type Данные нового типа тренировки.
     * @return Сохраненный тип тренировки.
     */
    TrainingType save(TrainingTypeDTO type);
}