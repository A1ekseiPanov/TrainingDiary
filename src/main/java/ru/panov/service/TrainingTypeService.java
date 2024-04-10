package ru.panov.service;

import ru.panov.model.TrainingType;
import ru.panov.model.dto.TrainingTypeDTO;

import java.util.List;

public interface TrainingTypeService {
    TrainingType findById(Long id);
    List<TrainingType> findAll();
    TrainingType save(TrainingTypeDTO type);
}