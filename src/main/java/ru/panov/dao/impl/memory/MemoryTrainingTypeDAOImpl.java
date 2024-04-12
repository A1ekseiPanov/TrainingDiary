package ru.panov.dao.impl.memory;

import ru.panov.dao.TrainingTypeDAO;
import ru.panov.model.TrainingType;

import java.util.*;

import static ru.panov.util.AutoIncrementUtil.increment;

/**
 * Реализация интерфейса TrainingTypeDAO для работы с типами тренировок в памяти.
 */
public class MemoryTrainingTypeDAOImpl implements TrainingTypeDAO {
    /**
     * Хрангилище типов тренировок.
     */
    private final Map<Long, TrainingType> types = Collections.synchronizedMap(new HashMap<>());

    /**
     * Конструктор класса. Создает несколько типов тренировок по умолчанию.
     */
    public MemoryTrainingTypeDAOImpl() {
        save(TrainingType.builder()
                .type("Кардио")
                .build());
        save(TrainingType.builder()
                .type("Силовая тренировка")
                .build());
        save(TrainingType.builder()
                .type("Йога")
                .build());
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        TrainingType type = types.get(id);
        return Optional.ofNullable(type);
    }

    @Override
    public List<TrainingType> findAll() {
        return List.copyOf(types.values());
    }

    @Override
    public TrainingType save(TrainingType type) {
        type.setId(increment(types));
        types.put(type.getId(), type);
        return type;
    }
}