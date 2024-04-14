package ru.panov.controller.factory;

import lombok.Getter;
import ru.panov.controller.AuditController;
import ru.panov.controller.TrainingController;
import ru.panov.controller.UserController;
import ru.panov.service.factory.ServiceFactory;

/**
 * Фабрика для олучения ссылок на обьекты контроллеров.
 * Содержит методы для получения экземпляров контроллеров для аудита, тренировок и пользователей.
 */
public final class ControllerFactory {
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private static final ControllerFactory INSTANCE = new ControllerFactory();
    @Getter
    private final AuditController auditController = new AuditController(serviceFactory.getAuditService());
    @Getter
    private final TrainingController trainingController = new TrainingController(
            serviceFactory.getTrainingService(), serviceFactory.getUserService(), serviceFactory.getTrainingTypeService());
    @Getter
    private final UserController userController = new UserController(serviceFactory.getUserService());


    public ControllerFactory() {
    }
    /**
     * Получает экземпляр фабрики контроллеров.
     *
     * @return Экземпляр фабрики контроллеров.
     */
    public static ControllerFactory getInstance() {
        return INSTANCE;
    }
}