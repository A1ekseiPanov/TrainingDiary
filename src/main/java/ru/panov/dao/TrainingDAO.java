package ru.panov.dao;

import ru.panov.model.Training;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс доступа к данным для сущности тренировки.
 */
public interface TrainingDAO {
    /**
     * Находит тренировку по её идентификатору и идентификатору пользователя.
     *
     * @param id     Идентификатор тренировки.
     * @param userId Идентификатор пользователя.
     * @return Optional, содержащий найденную тренировку, если такая существует, иначе пустой Optional.
     */
    Optional<Training> findById(Long id,Long userId);

    /**
     * Возвращает список всех тренировок.
     *
     * @return Список всех тренировок.
     */
    List<Training> findAll();

    /**
     * Сохраняет тренировку для указанного пользователя.
     *
     * @param entity Тренировка для сохранения.
     * @param userId Идентификатор пользователя.
     * @return Сохраненная тренировка.
     */
    Training save(Training entity, Long userId);

    /**
     * Обновляет тренировку для указанного пользователя.
     *
     * @param entity Сущность тренировки для обновления.
     * @param userId Идентификатор пользователя..
     * @return Обновленная сущность тренировки.
     */
    Training update(Training entity, Long userId);

    /**
     * Удаляет тренировку по её идентификатору и идентификатору пользователя.
     *
     * @param id     Идентификатор тренировки.
     * @param userId Идентификатор пользователя.
     */
    boolean delete(Long id, Long userId);

    /**
     * Рассчитывает количество сожженных калорий за определенный период для указанного пользователя.
     *
     * @param start  Начальная дата и время периода.
     * @param end    Конечная дата и время периода.
     * @param userId Идентификатор пользователя.
     * @return Количество сожженных калорий.
     */
    Double caloriesSpentOverPeriod(LocalDateTime start, LocalDateTime end, Long userId);

    /**
     * Возвращает список всех тренировок для указанного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список всех тренировок для указанного пользователя.
     */
    List<Training> findAllByUserId(Long userId);

    /**
     * Возвращает список тренировок с учетом пагинации.
     *
     * @param limit  Максимальное количество тренировок для возврата.
     * @param offset Смещение относительно начала списка тренировок.
     * @return Список тренировок с учетом указанной пагинации.
     */
    List<Training> findAll(int limit, int offset);
}