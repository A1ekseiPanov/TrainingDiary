package ru.panov.dao.factory;


import lombok.Getter;
import ru.panov.dao.AuditDAO;
import ru.panov.dao.TrainingDAO;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.dao.UserDAO;
import ru.panov.dao.impl.jdbc.JdbcAuditDAOImpl;
import ru.panov.dao.impl.jdbc.JdbcTrainingDAOImpl;
import ru.panov.dao.impl.jdbc.JdbcTrainingTypeDAOImpl;
import ru.panov.dao.impl.jdbc.JdbcUserDAOImpl;
import ru.panov.util.ConnectionUtil;

/**
 * Фабрика доступа к данным для создания объектов доступа к данным.
 * Предоставляет методы для получения экземпляров DAO для аудита, тренировок, типов тренировок и пользователей.
 */
@Getter
public final class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private final AuditDAO auditDAO = new JdbcAuditDAOImpl(ConnectionUtil.get());
    private final TrainingDAO trainingDAO = new JdbcTrainingDAOImpl(ConnectionUtil.get());
    private final TrainingTypeDAO trainingTypeDAO = new JdbcTrainingTypeDAOImpl(ConnectionUtil.get());
    private final UserDAO userDAO = new JdbcUserDAOImpl(ConnectionUtil.get());

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