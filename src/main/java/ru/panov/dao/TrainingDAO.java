package ru.panov.dao;

import ru.panov.model.Training;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainingDAO {
    Optional<Training> findById(Long id,Long userId);
    List<Training> findAll();
    Training save(Training entity, Long userId);
    void delete(Long id, Long userId);
    Double caloriesSpentOverPeriod(LocalDateTime start, LocalDateTime end, Long userId);
    List<Training> findAllByUserId(Long userId);
}