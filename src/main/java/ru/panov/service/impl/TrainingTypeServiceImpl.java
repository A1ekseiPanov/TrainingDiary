package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.exception.NotFoundException;
import ru.panov.model.TrainingType;
import ru.panov.service.TrainingTypeService;

import java.util.List;

@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;
    @Override
    public TrainingType findById(Long id)  {
        return trainingTypeDAO.findById(id)
                .orElseThrow(()-> new NotFoundException("Type training with id = %s, not found".formatted(id)));
    }

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeDAO.findAll();
    }

    @Override
    public TrainingType save(TrainingType type) {
        return trainingTypeDAO.save(type);
    }
}