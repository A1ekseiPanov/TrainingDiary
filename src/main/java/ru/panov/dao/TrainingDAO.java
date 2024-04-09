package ru.panov.dao;

import ru.panov.model.Training;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingDAO extends AbstractBaseDAO<Long, Training> {
    Training update(Long id, Training entity);
    void delete(Long id);
    Double caloriesSpentOverPeriod(LocalDateTime start, LocalDateTime end);
    List<Training> findAllByUserId(Long userId);
}