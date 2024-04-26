package ru.panov.service.impl;


import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingDAO;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.mapper.TrainingMapper;
import ru.panov.model.AuditType;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.User;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.model.dto.request.BurningCaloriesRequest;
import ru.panov.model.dto.response.TrainingResponse;
import ru.panov.service.AuditService;
import ru.panov.service.TrainingService;
import ru.panov.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.panov.util.DateTimeUtil.parseDateTimeFromString;
import static ru.panov.util.DateTimeUtil.parseTimeFromString;

/**
 * Реализация сервиса для работы с тренировками.
 */
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDAO trainingDAO;
    private final UserService userService;
    private final AuditService auditService;
    private static final TrainingMapper MAPPER = TrainingMapper.INSTANCE;

    @Override
    public List<TrainingResponse> findAll(Long userId) {
        User user = userService.getById(userId);
        if (checkUserIsLogged(userId)) {
            if (user.getRole().equals(Role.ADMIN)) {
                auditService.audit(this.getClass().getSimpleName(), "findAll",
                        AuditType.SUCCESS, user.getUsername());
                return MAPPER.toDtoResponseList(trainingDAO.findAll());
            }
            auditService.audit(this.getClass().getSimpleName(), "findAll(userId(%s))".formatted(userId),
                    AuditType.SUCCESS, user.getUsername());
            return MAPPER.toDtoResponseList(trainingDAO.findAllByUserId(userId));
        }
        return Collections.emptyList();
    }

    @Override
    public TrainingResponse findById(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(userId) && training.isPresent()) {
            auditService.audit(this.getClass().getSimpleName(), "findById",
                    AuditType.SUCCESS, "userId = %s".formatted(userId));
            return MAPPER.toResponseDTO(training.get());
        } else {
            auditService.audit(this.getClass().getSimpleName(), "findById",
                    AuditType.FAIL, "userId = %s".formatted(userId));
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }
    }

    @Override
    public void delete(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(userId) && training.isPresent()) {
            auditService.audit(this.getClass().getSimpleName(), "delete",
                    AuditType.SUCCESS, "userId = %s".formatted(userId));
            trainingDAO.delete(id, userId);
        } else {
            auditService.audit(this.getClass().getSimpleName(), "delete",
                    AuditType.FAIL, "userId = %s".formatted(userId));
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }
    }

    @Override
    public Training update(Long id, TrainingDTO trainingDTO, Long userId) {
        Optional<Training> trainingById = trainingDAO.findById(id, userId);

        if (trainingDTO.getCountCalories() < 0) {
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.FAIL, "userId = %s".formatted(userId));
            throw new ValidationException("Количество потраченных калорий должно быть больше 0.");
        }

        if (trainingById.isPresent() && checkUserIsLogged(userId)) {
            trainingById.get().setTypeId(trainingDTO.getTypeId());
            trainingById.get().setTrainingTime(parseTimeFromString(trainingDTO.getTimeTraining()));
            trainingById.get().setAdditionalInfo(trainingDTO.getAdditionalInformation());
            trainingById.get().setCountCalories(trainingDTO.getCountCalories());
            trainingById.get().setUpdated(LocalDateTime.now());
            auditService.audit(this.getClass().getSimpleName(), "update",
                    AuditType.SUCCESS, "userId = %s".formatted(userId));
            return trainingDAO.update(trainingById.get(), userId);
        } else {
            auditService.audit(this.getClass().getSimpleName(), "update",
                    AuditType.FAIL, "userId = %s".formatted(userId));
            throw new NotFoundException("Тренировка с id(%s) у пользователя с id(%s) не найдена".formatted(id, userId));
        }
    }

    @Override
    public Double caloriesSpentOverPeriod(BurningCaloriesRequest burningCaloriesRequest, Long userId) {
        auditService.audit(this.getClass().getSimpleName(), "caloriesSpentOverPeriod",
                AuditType.SUCCESS, "userId = %s".formatted(userId));
        return trainingDAO.caloriesSpentOverPeriod(parseDateTimeFromString(burningCaloriesRequest.getDateTimeStart()),
                parseDateTimeFromString(burningCaloriesRequest.getDateTimeEnd()), userId);
    }

    @Override
    public Training save(Long userId, TrainingDTO trainingDTO) {
        if (trainingDTO.getCountCalories() < 0) {
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.FAIL, "userId = %s".formatted(userId));
            throw new ValidationException("Количество потраченных калорий должно быть больше 0.");
        }

        List<Training> trainings = trainingDAO.findAllByUserId(userId);
        long count = trainings.stream()
                .filter(training -> training.getCreated().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .filter(training -> Objects.equals(training.getTypeId(), trainingDTO.getTypeId()))
                .count();

        if (count > 0) {
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.FAIL, "userId = %s".formatted(userId));
            throw new DuplicateException("Тренировка с данным типом сегодня уже была");
        }

        if (checkUserIsLogged(userId)) {
            Training training = Training.builder()
                    .typeId(trainingDTO.getTypeId())
                    .trainingTime(parseTimeFromString(trainingDTO.getTimeTraining()))
                    .additionalInfo(trainingDTO.getAdditionalInformation())
                    .countCalories(trainingDTO.getCountCalories())
                    .userId(userId)
                    .build();
            auditService.audit(this.getClass().getSimpleName(), "save",
                    AuditType.SUCCESS, "userId = %s".formatted(userId));
            return trainingDAO.save(training, userId);
        }
        return null;
    }

    private boolean checkUserIsLogged(Long userId) {
        User loggedUser = userService.getById(userId);
        return Objects.equals(userId, loggedUser.getId());
    }
}