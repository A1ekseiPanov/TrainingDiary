package ru.panov.in.view;

import lombok.RequiredArgsConstructor;
import ru.panov.controller.AuditController;
import ru.panov.controller.TrainingController;
import ru.panov.controller.UserController;
import ru.panov.model.Audit;
import ru.panov.model.dto.TrainingTypeDTO;
import ru.panov.util.ViewUtil;
import ru.panov.in.view.factory.ViewFactory;

import java.util.List;
import java.util.Scanner;


/**
 * Класс AdminView представляет пользовательский интерфейс для взаимодействия с функционалом приложения для администратора.
 */
@RequiredArgsConstructor
public class AdminView {
    private final TrainingController trainingController;
    private final AuditController auditController;
    private final UserController userController;

    /**
     * Запускает меню администратора.
     *
     * @param scanner объект Scanner для ввода пользователя
     */
    public void runAdminMenu(Scanner scanner) {

        while (true) {
            printLoggedAdminInMenu();
            int choice = ViewUtil.getUserChoice(scanner);

            switch (choice) {
                case 1:
                    ViewFactory.getInstance().getUserView().printHistoryTraining(trainingController.getAllTraining(), scanner);
                    break;
                case 2:
                    ViewFactory.getInstance().getUserView().printTrainingTypes();
                    runChangeTrainingTypeMenu(scanner);
                    break;
                case 3:
                    printAudit(auditController.getAllAudits());
                    break;

                case 4:
                    userController.logout();
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    /**
     * Выводит на экран аудит действий пользователей.
     *
     * @param audits список аудита действий пользователей
     */
    private void printAudit(List<Audit> audits) {
        audits.forEach(System.out::println);
    }

    /**
     * Выводит на экран главное меню администратора.
     */
    private void printLoggedAdminInMenu() {
        System.out.println("Меню администратора:");
        System.out.println("1. Тренировки пользователей");
        System.out.println("2. Типы тренировок");
        System.out.println("3. Аудит действий пользователей");
        System.out.println("4. Выход из системы");
    }

    /**
     * Выводит на экран меню добавления типов тренировок.
     */
    private void printChangeTrainingTypesAdmin() {
        System.out.println("1. Добавить тип тренировок");
        System.out.println("2. Вернуться назад");
    }

    /**
     * Выводит на экран меню добовления типов тренировок.
     *
     * @param scanner объект Scanner для ввода пользователя
     */
    public void runChangeTrainingTypeMenu(Scanner scanner) {
        while (true) {
            printChangeTrainingTypesAdmin();
            int choice = ViewUtil.getUserChoice(scanner);
            switch (choice) {
                case 1:
                    createTrainingType(scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    /**
     * Создает новый тип тренировки.
     *
     * @param scanner объект Scanner для ввода пользователя
     */
    private void createTrainingType(Scanner scanner) {
        System.out.println("Добавление нового типа тренировок");
        System.out.println("Тип тренировки");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.nextLine();
        }
        scanner.nextLine();
        String type = scanner.nextLine();
        trainingController.createTypeTraining(TrainingTypeDTO.builder()
                .type(type)
                .build());
    }
}