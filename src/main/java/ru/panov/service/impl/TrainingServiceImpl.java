package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingDAO;
import ru.panov.exception.NotFoundException;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.User;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.service.TrainingService;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

import static ru.panov.util.DateTimeUtil.parseDateTimeFromString;
import static ru.panov.util.DateTimeUtil.parseTimeFromString;

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDAO trainingDAO;
    private final UserService userService;
    private final TrainingTypeService typeService;

    @Override
    public List<Training> findAll(Long userId) {
        User user = userService.getLoggedUser();
        if (checkUserIsLogged(userId)) {
            if (user.getRole().equals(Role.ADMIN)) {
                return trainingDAO.findAll();
            }
            return trainingDAO.findAllByUserId(userId);
        }
        return Collections.emptyList();
    }

    @Override
    public Training findById(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(id) && training.isPresent()) {
            return training.get();
        } else {
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }
    }

    @Override
    public void delete(Long userId, Long id) {
        Optional<Training> training = trainingDAO.findById(id, userId);
        if (checkUserIsLogged(userId) && training.isPresent()) {
            trainingDAO.delete(id, userId);
        } else {
            throw new NotFoundException("Тренировка с id=%s у пользователя с id=%s не найдена".formatted(id, userId));
        }

    }

    @Override
    public Training update(Long id, TrainingDTO trainingDTO, Long userId) {
        Optional<Training> trainingById = trainingDAO.findById(id, userId);

        if (trainingById.isPresent()&&checkUserIsLogged(userId)) {
            trainingById.get().setType(typeService.findById(trainingDTO.getTypeId()));
            trainingById.get().setTimeTraining(parseTimeFromString(trainingDTO.getTimeTraining()));
            trainingById.get().setAdditionalInformation(trainingDTO.getAdditionalInformation());
            trainingById.get().setCountCalories(trainingDTO.getCountCalories());
            trainingById.get().setUpdated(LocalDateTime.now());
            return trainingDAO.save(trainingById.get(), userId);
        } else {
            throw new NotFoundException("Тренировка с id(%s) у пользователя с id(%s) не найдена".formatted(id, userId));
        }
    }

    @Override
    public Double caloriesSpentOverPeriod(String dateTimeStart, String dateTimeEnd, Long userId) {
        return trainingDAO.caloriesSpentOverPeriod(parseDateTimeFromString(dateTimeStart),
                parseDateTimeFromString(dateTimeEnd), userId);

    }

    @Override
    public Training save(Long userId, TrainingDTO trainingDTO) {
        if (checkUserIsLogged(userId)) {
            Training training = Training.builder().
                    type(typeService.findById(trainingDTO.getTypeId()))
                    .timeTraining(parseTimeFromString(trainingDTO.getTimeTraining()))
                    .additionalInformation(trainingDTO.getAdditionalInformation())
                    .countCalories(trainingDTO.getCountCalories())
                    .userId(userId)
                    .build();
            return trainingDAO.save(training, userId);
        }
        return null;
    }

    private boolean checkUserIsLogged(Long userId) {
        User loggetUser = userService.getLoggedUser();
        return Objects.equals(userId, loggetUser.getId());
    }
}
