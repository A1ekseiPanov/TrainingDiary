package ru.panov.dao.impl.memory;

import ru.panov.dao.TrainingDAO;
import ru.panov.model.Training;

import java.time.LocalDateTime;
import java.util.*;

import static ru.panov.util.AutoIncrementUtil.increment;

public class MemoryTrainingDAOImpl implements TrainingDAO {

    private final Map<Long, Training> trainings = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<Training> findById(Long id, Long userId) {
        Training training = trainings.get(id);
        if (training != null && Objects.equals(training.getUserId(), userId)) {
            return Optional.of(training);
        }
        return Optional.empty();
    }

    @Override
    public List<Training> findAll() {
        return List.copyOf(trainings.values().stream()
                .sorted(Comparator.comparing(Training::getCreated))
                .toList());

    }

    @Override
    public Training save(Training training, Long userId) {
        if (training.getId() == null) {
            training.setId(increment(trainings));
            training.setUserId(userId);
        }
        trainings.put(training.getId(), training);
        return training;
    }

    @Override
    public void delete(Long id, Long userId) {
        Training training = trainings.get(id);
        if (training != null && Objects.equals(training.getUserId(), userId)) {
            trainings.remove(id);
        }

    }

    @Override
    public Double caloriesSpentOverPeriod(LocalDateTime start, LocalDateTime end, Long userId) {
        return trainings.values().stream()
                .filter(training -> Objects.equals(training.getUserId(), userId))
                .filter(training -> training.getCreated().isAfter(start))
                .filter(training -> training.getCreated().isBefore(end))
                .mapToDouble(Training::getCountCalories)
                .sum();
    }

    @Override
    public List<Training> findAllByUserId(Long userId) {
        return List.copyOf(trainings.values().stream()
                .filter(training -> Objects.equals(userId, training.getUserId()))
                .sorted(Comparator.comparing(Training::getCreated))
                .toList());
    }
}