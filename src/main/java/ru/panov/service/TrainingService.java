package ru.panov.service;

import ru.panov.model.Training;

import java.util.List;

public interface TrainingService {

    List<Training> findAll(Long userId);

    Training findById(Long userId, Long id);

    void delete(Long userId, Long id);

    Training update(Long id, Long userId);

    Double caloriesSpentOverPeriod(int monthStart, int dayStart, int monthEnd, int dayEnd, Long userId);

    Training save(Long userId, Training training);
}