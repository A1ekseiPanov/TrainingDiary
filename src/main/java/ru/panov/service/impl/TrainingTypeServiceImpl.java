package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.exception.NotFoundException;
import ru.panov.model.AuditType;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.TrainingTypeDTO;
import ru.panov.service.AuditService;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;

import java.util.List;

/**
 * Реализация сервиса для работы с типами тренировок.
 */
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;
    private final AuditService auditService;
    private final UserService userService;

    @Override
    public TrainingType findById(Long id) {
        return trainingTypeDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Тип тренировки с id = %s, не найден".formatted(id)));
    }

    @Override
    public List<TrainingType> findAll() {
        auditService.audit(this.getClass().getSimpleName(), "findAll",
                AuditType.SUCCESS, userService.getLoggedUser().getUsername());
        return trainingTypeDAO.findAll();
    }

    @Override
    public TrainingType save(TrainingTypeDTO typeDTO) {
        TrainingType type = TrainingType.builder()
                .type(typeDTO.getType())
                .build();
        auditService.audit(this.getClass().getSimpleName(), "save",
                AuditType.SUCCESS, userService.getLoggedUser().getUsername());
        return trainingTypeDAO.save(type);
    }
}