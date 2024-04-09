package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingDAO;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.User;
import ru.panov.service.TrainingService;
import ru.panov.service.UserService;

import java.util.List;

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDAO trainingDAO;
    private final UserService userService;
    @Override
    public List<Training> findAll(Long userId) {
        User user = userService.getById(userId);
        if(user.getRole().equals(Role.ADMIN)){
            return trainingDAO.findAll();
        }
        return trainingDAO.findAllByUserId(userId);
    }

    @Override
    public Training findById(Long userId, Long id) {
        return null;
    }

    @Override
    public void delete(Long userId, Long id) {

    }

    @Override
    public Training update(Long id, Long userId) {
        return null;
    }

    @Override
    public Double caloriesSpentOverPeriod(int monthStart, int dayStart, int monthEnd, int dayEnd, Long userId) {
        return null;
    }

    @Override
    public Training save(Long userId, Training training) {
        return null;
    }
}
