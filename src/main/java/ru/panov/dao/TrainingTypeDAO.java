package ru.panov.dao;

import ru.panov.model.TrainingType;

import java.util.Optional;

/**
 * Интерфейс доступа к данным для сущности типа тренировки.
 */
public interface TrainingTypeDAO extends AbstractBaseDAO<Long, TrainingType> {
    /**
     * Поиск типа тренировки по его названию.
     *
     * @param type Название типа тренировки для поиска.
     * @return Optional, содержащий найденный тип тренировки, если такой существует, иначе пустой Optional.
     */
    Optional<TrainingType> findByType(String type);
}