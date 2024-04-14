package ru.panov.dao;

import java.util.List;
import java.util.Optional;

/**
 * Общий интерфейс для доступа к данным.
 *
 * @param <K> Тип идентификатора сущности.
 * @param <E> Тип сущности.
 */
public interface AbstractBaseDAO<K, E> {
    /**
     * Находит сущность по её идентификатору.
     *
     * @param id Идентификатор сущности.
     * @return Optional, содержащий найденную сущность, если такая существует, иначе пустой Optional.
     */
    Optional<E> findById(K id);
    /**
     * Возвращает список всех сущностей.
     *
     * @return Список всех сущностей.
     */
    List<E> findAll();
    /**
     * Сохраняет сущность.
     *
     * @param entity Сущность для сохранения.
     * @return Сохраненная сущность.
     */
    E save(E entity);
}