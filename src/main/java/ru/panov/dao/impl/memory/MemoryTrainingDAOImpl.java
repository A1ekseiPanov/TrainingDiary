package ru.panov.dao.impl.memory;

import ru.panov.dao.TrainingDAO;
import ru.panov.exception.NotFoundException;
import ru.panov.model.Training;

import java.time.LocalDateTime;
import java.util.*;

import static ru.panov.util.AutoIncrementUtil.increment;

public class MemoryTrainingDAOImpl implements TrainingDAO {

    private final Map<Long, Training> trainings = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<Training> findById(Long id) {
        Training training = trainings.get(id);
        return Optional.ofNullable(training);
    }

    @Override
    public List<Training> findAll() {
        return List.copyOf(trainings.values().stream()
                .sorted(Comparator.comparing(Training::getCreated))
                .toList());

    }

    @Override
    public Training save(Training training) {
        training.setId(increment(trainings));
        trainings.put(training.getId(), training);
        return training;
    }


    @Override
    public Training update(Long id, Training training) {
        if (trainings.containsKey(id)) {
            training.setId(id);
            trainings.put(id, training);
            return training;
        }
        throw new NotFoundException("Training with id %s not found".formatted(id));
    }

    @Override
    public void delete(Long id) {
        trainings.remove(id);

    }

    @Override
    public Double caloriesSpentOverPeriod(LocalDateTime start, LocalDateTime end) {
        return trainings.values().stream()
                .filter(training -> training.getCreated().isAfter(start))
                .filter(training -> training.getCreated().isBefore(end))
                .mapToDouble(Training::getCountCalories)
                .sum();
    }

    @Override
    public List<Training> findAllByUserId(Long userId) {
        return List.copyOf(trainings.values().stream()
                .filter(training -> Objects.equals(userId, training.getUser().getId()))
                .toList());
    }
}