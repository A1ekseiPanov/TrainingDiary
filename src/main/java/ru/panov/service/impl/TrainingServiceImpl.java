package ru.panov.service.impl;


import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingDAO;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.model.AuditType;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.User;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.service.AuditService;
import ru.panov.service.TrainingService;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;
import ru.panov.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Реализация сервиса для работы с тренировками.
 */
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDAO trainingDAO;
    private final UserService userService;
    private final TrainingTypeService typeService;
    private final AuditService auditService;

    @Override
    public List<Training> findAll(Long userId) {
        User user = userService.getLoggedUser();
        if (checkUserIsLogged(userId)) {
            if (user.getRole().equals(Role.ADMIN)) {
                return trainingDAO.findAll();
            }
            return trainingDAO.findAllByUserId(userId);
        }
        auditService.audit(this.getClass().getSimpleName(), "findAll",
                AuditType.SUCCESS, userService.getLoggedUser().getUsername());
        return Collections.emptyList();
    }

    @Override
    public Training findById(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(userId) && training.isPresent()) {
            auditService.audit(this.getClass().getSimpleName(), "findById",
                    AuditType.SUCCESS, userService.getLoggedUser().getUsername());
            return training.get();
        } else {
            auditService.audit(this.getClass().getSimpleName(), "findById",
                    AuditType.FAIL, userService.getLoggedUser().getUsername());
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }
    }

    @Override
    public void delete(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(userId) && training.isPresent()) {
            auditService.audit(this.getClass().getSimpleName(), "delete",
                    AuditType.SUCCESS, userService.getLoggedUser().getUsername());
            trainingDAO.delete(id, userId);
        } else {
            auditService.audit(this.getClass().getSimpleName(), "delete",
                    AuditType.FAIL, userService.getLoggedUser().getUsername());
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }

    }

    @Override
    public Training update(Long id, TrainingDTO trainingDTO, Long userId) {
        Optional<Training> trainingById = trainingDAO.findById(id, userId);

        if (trainingById.isPresent() && checkUserIsLogged(userId)) {
            trainingById.get().setType(typeService.findById(trainingDTO.getTypeId()));
            trainingById.get().setTrainingTime(DateTimeUtil.parseTimeFromString(trainingDTO.getTimeTraining()));
            trainingById.get().setAdditionalInfo(trainingDTO.getAdditionalInformation());
            trainingById.get().setCountCalories(trainingDTO.getCountCalories());
            trainingById.get().setUpdated(LocalDateTime.now());
            auditService.audit(this.getClass().getSimpleName(), "update",
                    AuditType.SUCCESS, userService.getLoggedUser().getUsername());
            return trainingDAO.save(trainingById.get(), userId);
        } else {
            auditService.audit(this.getClass().getSimpleName(), "update",
                    AuditType.FAIL, userService.getLoggedUser().getUsername());
            throw new NotFoundException("Тренировка с id(%s) у пользователя с id(%s) не найдена".formatted(id, userId));
        }
    }

    @Override
    public Double caloriesSpentOverPeriod(String dateTimeStart, String dateTimeEnd, Long userId) {
        auditService.audit(this.getClass().getSimpleName(), "caloriesSpentOverPeriod",
                AuditType.SUCCESS, userService.getLoggedUser().getUsername());
        return trainingDAO.caloriesSpentOverPeriod(DateTimeUtil.parseDateTimeFromString(dateTimeStart),
                DateTimeUtil.parseDateTimeFromString(dateTimeEnd), userId);

    }

    @Override
    public Training save(Long userId, TrainingDTO trainingDTO) {
        List<Training> trainings = trainingDAO.findAllByUserId(userId);
        long count = trainings.stream()
                .filter(training -> training.getCreated().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .filter(training -> Objects.equals(training.getType().getId(), trainingDTO.getTypeId()))
                .count();
        if (count > 0) {
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.FAIL, userService.getLoggedUser().getUsername());
            throw new DuplicateException("Тренировка с данным типом сегодня уже была");
        }
        if (checkUserIsLogged(userId)) {
            Training training = Training.builder().
                    type(typeService.findById(trainingDTO.getTypeId()))
                    .trainingTime(DateTimeUtil.parseTimeFromString(trainingDTO.getTimeTraining()))
                    .additionalInfo(trainingDTO.getAdditionalInformation())
                    .countCalories(trainingDTO.getCountCalories())
                    .userId(userId)
                    .build();
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.SUCCESS, userService.getLoggedUser().getUsername());
            return trainingDAO.save(training, userId);
        }
        return null;
    }

    private boolean checkUserIsLogged(Long userId) {
        User loggedUser = userService.getLoggedUser();
        return Objects.equals(userId, loggedUser.getId());
    }
}