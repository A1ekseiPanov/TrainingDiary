package ru.panov.service;

import ru.panov.model.Training;
import ru.panov.model.dto.TrainingDTO;

import java.util.List;

public interface TrainingService {

    List<Training> findAll(Long userId);

    Training findById(Long userId, Long id);

    void delete(Long userId, Long id);

    Training update(Long id, TrainingDTO training, Long userId);

    Double caloriesSpentOverPeriod(String dateTimeStart, String dateTimeEnd,Long userId);

    Training save(Long userId, TrainingDTO training);
}