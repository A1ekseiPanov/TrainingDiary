package ru.panov.dao.factory;


import lombok.Getter;
import ru.panov.dao.AuditDAO;
import ru.panov.dao.TrainingDAO;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.dao.UserDAO;
import ru.panov.dao.impl.memory.MemoryAuditDAOImpl;
import ru.panov.dao.impl.memory.MemoryTrainingDAOImpl;
import ru.panov.dao.impl.memory.MemoryTrainingTypeDAOImpl;
import ru.panov.dao.impl.memory.MemoryUserDAOImpl;

/**
 * Фабрика доступа к данным для создания объектов доступа к данным.
 * Предоставляет методы для получения экземпляров DAO для аудита, тренировок, типов тренировок и пользователей.
 */
@Getter
public final class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private final AuditDAO auditDAO = new MemoryAuditDAOImpl();
    private final TrainingDAO trainingDAO = new MemoryTrainingDAOImpl();
    private final TrainingTypeDAO trainingTypeDAO = new MemoryTrainingTypeDAOImpl();
    private final UserDAO userDAO = new MemoryUserDAOImpl();

    private DAOFactory() {
    }

    /**
     * Получает экземпляр фабрики DAO.
     *
     * @return Экземпляр фабрики DAO.
     */
    public static DAOFactory getInstance() {
        return INSTANCE;
    }
}