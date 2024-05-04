package ru.panov.service.factory;

import lombok.Getter;
import ru.panov.dao.factory.DAOFactory;
import ru.panov.security.factory.SecurityFactory;
import ru.panov.service.AuditService;
import ru.panov.service.TrainingService;
import ru.panov.service.TrainingTypeService;
import ru.panov.service.UserService;
import ru.panov.service.impl.AuditServiceImpl;
import ru.panov.service.impl.TrainingServiceImpl;
import ru.panov.service.impl.TrainingTypeServiceImpl;
import ru.panov.service.impl.UserServiceImpl;

/**
 * Фабрика для создания экземпляров сервисов.
 */
public final class ServiceFactory {
    private final DAOFactory daoFactory = DAOFactory.getInstance();
    private final SecurityFactory securityFactory = SecurityFactory.getInstance();

    private static final ServiceFactory INSTANCE = new ServiceFactory();

    @Getter
    private final AuditService auditService = new AuditServiceImpl(daoFactory.getAuditDAO());
    @Getter
    private final UserService userService = new UserServiceImpl(daoFactory.getUserDAO(), securityFactory.getJwtService());

    @Getter
    private final TrainingTypeService trainingTypeService = new TrainingTypeServiceImpl(
            daoFactory.getTrainingTypeDAO());
    @Getter
    private final TrainingService trainingService = new TrainingServiceImpl(daoFactory.getTrainingDAO(),
            getUserService());

    private ServiceFactory() {
    }

    /**
     * Получить единственный экземпляр фабрики сервисов.
     *
     * @return Единственный экземпляр фабрики сервисов.
     */
    public static ServiceFactory getInstance() {
        return INSTANCE;
    }
}