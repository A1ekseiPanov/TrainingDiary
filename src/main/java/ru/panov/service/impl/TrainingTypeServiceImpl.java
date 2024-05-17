package ru.panov.service.impl;

import annotations.Audit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.mapper.TrainingTypeMapper;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.request.TrainingTypeRequest;
import ru.panov.model.dto.response.TrainingTypeResponse;
import ru.panov.service.TrainingTypeService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с типами тренировок.
 */
@Service
@RequiredArgsConstructor
@Audit
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;
    private final TrainingTypeMapper mapper;

    @Override
    public TrainingTypeResponse findById(Long id) {
        return mapper.toResponseEntity(trainingTypeDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Тип тренировки с id = %s, не найден".formatted(id))));
    }

    @Override
    public List<TrainingTypeResponse> findAll() {
        return mapper.toResponseEntityList(trainingTypeDAO.findAll());
    }

    @Override
    public TrainingTypeResponse save(TrainingTypeRequest typeDTO) {
        Optional<TrainingType> trainingType = trainingTypeDAO.findByType(typeDTO.getType());

        if (trainingType.isEmpty()) {
            TrainingType type = TrainingType.builder()
                    .type(typeDTO.getType())
                    .build();

            return mapper.toResponseEntity(trainingTypeDAO.save(type));
        } else {
            throw new DuplicateException("Тип с названием %s существует".formatted(typeDTO.getType()));
        }
    }
}