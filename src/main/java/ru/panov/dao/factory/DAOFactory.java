package ru.panov.dao.factory;


import ru.panov.dao.AuditDAO;
import ru.panov.dao.TrainingDAO;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.dao.UserDAO;
import ru.panov.dao.impl.memory.MemoryAuditDAOImpl;
import ru.panov.dao.impl.memory.MemoryTrainingDAOImpl;
import ru.panov.dao.impl.memory.MemoryTrainingTypeDAOImpl;
import ru.panov.dao.impl.memory.MemoryUserDAOImpl;

public final class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private final AuditDAO auditDAO = new MemoryAuditDAOImpl();
    private final TrainingDAO trainingDAO= new MemoryTrainingDAOImpl();
    private final TrainingTypeDAO trainingTypeDAO = new MemoryTrainingTypeDAOImpl();
    private final UserDAO userDAO = new MemoryUserDAOImpl();
    private DAOFactory() {
    }
    public static DAOFactory getInstance(){ return INSTANCE; }

    public AuditDAO getAuditDAO() {
        return auditDAO;
    }

    public TrainingDAO getTrainingDAO() {
        return trainingDAO;
    }

    public TrainingTypeDAO getTrainingTypeDAO() {
        return trainingTypeDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
