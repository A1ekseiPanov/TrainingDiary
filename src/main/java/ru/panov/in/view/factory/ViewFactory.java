package ru.panov.in.view.factory;


import lombok.Getter;
import ru.panov.controller.factory.ControllerFactory;
import ru.panov.in.view.AdminView;
import ru.panov.in.view.AuthView;
import ru.panov.in.view.UserView;

/**
 * Фабрика представлений.
 */
public final class ViewFactory {

    private final ControllerFactory controllerFactory = ControllerFactory.getInstance();
    private static final ViewFactory INSTANCE = new ViewFactory();
    @Getter
    private final AdminView adminView = new AdminView(controllerFactory.getTrainingController(),
            controllerFactory.getAuditController(), controllerFactory.getUserController());
    @Getter
    private final AuthView authView = new AuthView(controllerFactory.getUserController());
    @Getter
    private final UserView userView = new UserView(controllerFactory.getUserController()
            , controllerFactory.getTrainingController());

    private ViewFactory() {
    }

    /**
     * Получает экземпляр фабрики представлений.
     *
     * @return экземпляр фабрики представлений
     */
    public static ViewFactory getInstance() {
        return INSTANCE;
    }
}