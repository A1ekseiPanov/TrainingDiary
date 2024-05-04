package ru.panov.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.panov.annotations.Audit;
import ru.panov.dao.TrainingDAO;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.exception.ValidationException;
import ru.panov.mapper.TrainingMapper;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.User;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.model.dto.request.BurningCaloriesRequest;
import ru.panov.model.dto.response.TrainingResponse;
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
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDAO trainingDAO;
    private final UserService userService;
    private static final TrainingMapper MAPPER = TrainingMapper.INSTANCE;

    @Override
    @Audit(username = "@userId")
    public List<TrainingResponse> findAll(Long userId) {
        User user = userService.getById(userId);
        if (checkUserIsLogged(userId)) {
            if (user.getRole().equals(Role.ADMIN)) {
                return MAPPER.toDtoResponseList(trainingDAO.findAll());
            }
            return MAPPER.toDtoResponseList(trainingDAO.findAllByUserId(userId));
        }
        return Collections.emptyList();
    }

    @Override
    @Audit(username = "@userId")
    public TrainingResponse findById(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(userId) && training.isPresent()) {
            return MAPPER.toResponseDTO(training.get());
        } else {
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }
    }

    @Override
    @Audit(username = "@userId")
    public void delete(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(userId) && training.isPresent()) {
            trainingDAO.delete(id, userId);
        } else {
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }
    }

    @Override
    @Audit(username = "@userId")
    public Training update(Long id, TrainingDTO trainingDTO, Long userId) {
        Optional<Training> trainingById = trainingDAO.findById(id, userId);

        if (trainingDTO.getCountCalories() < 0) {
            throw new ValidationException("Количество потраченных калорий должно быть больше 0.");
        }

        if (trainingById.isPresent() && checkUserIsLogged(userId)) {
            trainingById.get().setTypeId(trainingDTO.getTypeId());
            trainingById.get().setTrainingTime(parseTimeFromString(trainingDTO.getTimeTraining()));
            trainingById.get().setAdditionalInfo(trainingDTO.getAdditionalInformation());
            trainingById.get().setCountCalories(trainingDTO.getCountCalories());
            trainingById.get().setUpdated(LocalDateTime.now());
            return trainingDAO.update(trainingById.get(), userId);
        } else {
            throw new NotFoundException("Тренировка с id(%s) у пользователя с id(%s) не найдена".formatted(id, userId));
        }
    }

    @Override
    @Audit(username = "@userId")
    public Double caloriesSpentOverPeriod(BurningCaloriesRequest burningCaloriesRequest, Long userId) {
        return trainingDAO.caloriesSpentOverPeriod(parseDateTimeFromString(burningCaloriesRequest.getDateTimeStart()),
                parseDateTimeFromString(burningCaloriesRequest.getDateTimeEnd()), userId);
    }

    @Override
    @Audit(username = "@userId")
    public Training save(Long userId, TrainingDTO trainingDTO) {
        if (trainingDTO.getCountCalories() < 0) {
            throw new ValidationException("Количество потраченных калорий должно быть больше 0.");
        }

        List<Training> trainings = trainingDAO.findAllByUserId(userId);
        long count = trainings.stream()
                .filter(training -> training.getCreated().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .filter(training -> Objects.equals(training.getTypeId(), trainingDTO.getTypeId()))
                .count();

        if (count > 0) {
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
            return trainingDAO.save(training, userId);
        }
        return null;
    }

    private boolean checkUserIsLogged(Long userId) {
        User loggedUser = userService.getById(userId);
        return Objects.equals(userId, loggedUser.getId());
    }
}