package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.mapper.TrainingTypeMapper;
import ru.panov.model.AuditType;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.request.TrainingTypeRequest;
import ru.panov.model.dto.response.TrainingTypeResponse;
import ru.panov.service.AuditService;
import ru.panov.service.TrainingTypeService;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Реализация сервиса для работы с типами тренировок.
 */
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;
    private final AuditService auditService;

    private static final TrainingTypeMapper MAPPER = TrainingTypeMapper.INSTANCE;

    @Override
    public TrainingTypeResponse findById(Long id) {
        return MAPPER.toResponseEntity(trainingTypeDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Тип тренировки с id = %s, не найден".formatted(id))));
    }

    @Override
    public List<TrainingTypeResponse> findAll() {
        auditService.audit(this.getClass().getSimpleName(), "findAll",
                AuditType.SUCCESS, "");
        return MAPPER.toResponseEntityList(trainingTypeDAO.findAll());
    }

    @Override
    public TrainingTypeResponse save(TrainingTypeRequest typeDTO) {
        if (isBlank(typeDTO.getType())) {
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.FAIL, "admin");
            throw new ValidationException("Название типа тренировки не может быть пустым или состоять только из пробелов.");
        }

        if (typeDTO.getType().length() < 2) {
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.FAIL, "admin");
            throw new ValidationException("Длина названия типа тренировки должна составлять минимум от 2 символов.");
        }
        Optional<TrainingType> trainingType = trainingTypeDAO.findByType(typeDTO.getType());

        if (trainingType.isEmpty()) {
            TrainingType type = TrainingType.builder()
                    .type(typeDTO.getType())
                    .build();
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.SUCCESS, "admin");

            return MAPPER.toResponseEntity(trainingTypeDAO.save(type));
        } else {
            throw new DuplicateException("Тип с названием %s существует".formatted(typeDTO.getType()));
        }
    }
}