package ru.panov.view;

import lombok.RequiredArgsConstructor;
import ru.panov.controller.AuditController;
import ru.panov.controller.TrainingController;
import ru.panov.controller.UserController;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.TrainingDTO;
import ru.panov.model.dto.TrainingTypeDTO;
import ru.panov.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@RequiredArgsConstructor
public class UserView {
    private final Scanner scanner;
    private final UserController userController;
    private final TrainingController trainingController;
    private final AuditController auditController;

    private int getUserChoice() {
        System.out.print("Выберете пункт из меню: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private void printMainMenu() {
        System.out.println("Training Diary Menu:");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход в систему");
        System.out.println("3. Выход");
    }

    public void runMainMenu() {
        while (true) {
            printMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Выход из приложения. До свидания!");
                    System.exit(0);
                }
                default -> System.out.println("Такого пункта меню не существует. Пожалуйста, попробуйте снова.");
            }
        }
    }


    public void registerUser() {
        System.out.println("Регистрация:");
        System.out.print("Введите username: ");
        String username = scanner.next();
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        userController.register(UserDTO.builder()
                .username(username)
                .password(password)
                .build());
    }

    public void login() {
        System.out.println("Вход:");
        System.out.print("Введите username: ");
        String username = scanner.next();
        System.out.print("Введите пароль: ");
        String password = scanner.next();
        userController.login(UserDTO.builder()
                .username(username)
                .password(password)
                .build());
        if (userController.loggedUser().getRole().equals(Role.USER)) {
            runUserMenu();
        } else {
            runAdminMenu();
        }
    }

    public void runUserMenu() {
        while (true) {
            printLoggedInUserMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createTraining();
                    break;
                case 2:
                    printHistoryTraining(trainingController.getAllTraining());
                    break;
                case 3:
                    staticTraining();
                    break;

                case 4:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printLoggedInUserMenu() {
        System.out.println("Меню пользователя:");
        System.out.println("1. Добавить тренировку");
        System.out.println("2. История тренировок");
        System.out.println("3. Статистика по расходу калорий за выбранный период");
        System.out.println("4. Выход из системы");
    }

    private TrainingDTO createUpdateData() {
        System.out.println("Тип тренировки:");
        List<TrainingType> typesTraining = printTrainingTypes();
        Optional<TrainingType> trainingType;
        while (true) {
            int choice = getUserChoice();
            trainingType = typesTraining.stream()
                    .filter(type -> type.getId() == choice).findFirst();
            if (trainingType.isEmpty()) {
                System.out.println("Не верный выбор");
            } else {
                break;
            }
        }
        System.out.println("Потрачено калорий:");
        while (!scanner.hasNextDouble()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        Double calories = scanner.nextDouble();

        System.out.println("Время тренировки(HH:mm:ss):");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        String timeTraining = scanner.nextLine();

        System.out.println("Дополнительная информация:");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        String additionalInformation = scanner.nextLine();
        return TrainingDTO.builder()
                .typeId(trainingType.get().getId())
                .countCalories(calories)
                .timeTraining(timeTraining)
                .additionalInformation(additionalInformation)
                .build();
    }

    private void createTraining() {
        trainingController.createTraining(createUpdateData());
    }

    private List<TrainingType> printTrainingTypes() {
        List<TrainingType> trainingTypes = trainingController.getAllTrainingType();
        trainingTypes.forEach(type -> System.out.println(type.getId() + ". " + type.getType()));
        return trainingTypes;
    }

    private void printHistoryTraining(List<Training> trainings) {
        trainings.forEach(training -> {
            printTraining(training);
            if (userController.loggedUser().getRole().equals(Role.USER)) {
                runTrainingMenu();
            }
        });
    }

    private void staticTraining() {
        System.out.println("Укажите периодд за каоторый необходима статистика");
        System.out.println("Дата и время начальное(dd-MM-yyyy HH:mm)");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.nextLine();
        }
        String start = scanner.nextLine();
        System.out.println("Дата и время конечные(dd-MM-yyyy HH:mm)");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.nextLine();
        }
        String end = scanner.nextLine();
        System.out.println("Потрачено калорий: " + trainingController.caloriesSpentOverPeriod(start, end));
    }


    private void printTraining(Training training) {
        System.out.println("Id: " + training.getId());
        System.out.println("Тип тренировки: " + training.getType().getType());
        System.out.println("Потрачено калорий: " + training.getCountCalories());
        System.out.println("Время тренировки: " + training.getTimeTraining());
        System.out.println("Дополнительная информация: " + training.getAdditionalInformation());
        System.out.println("Дата тренировки: " + training.getCreated());
        System.out.println("Дата последнего редактирования: " + training.getUpdated());
        System.out.println("-------------");
    }


    public void printAddedTrainingUserMenu() {
        System.out.println("1. Просмотр тренировки");
        System.out.println("2. Вернуться назад");
    }

    public void runTrainingMenu() {
        while (true) {
            printAddedTrainingUserMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    choosingTraining();
                    break;
                case 2:
                    printHistoryTraining(trainingController.getAllTraining());
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void choosingTraining() {
        System.out.println("Выбирите тренировку по id");
        int choice = getUserChoice();
        Training training = trainingController.getByTrainingById((long) choice);
        printTraining(training);
        runChangeTrainingMenu(training.getId());
    }

    void printManipulationTrainingUserMenu() {
        System.out.println("1. Редактировать тренировку");
        System.out.println("2. Удалить тренировку");
        System.out.println("3. Вернуться назад");
    }

    public void runChangeTrainingMenu(Long trainingId) {
        while (true) {
            printManipulationTrainingUserMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    updatingTraining(trainingId);
                    break;
                case 2:
                    deleteTraining(trainingId);
                    break;
                case 3:
                    printHistoryTraining(trainingController.getAllTraining());
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void deleteTraining(Long trainingId) {
        trainingController.deleteTraining(trainingId);
    }

    private void updatingTraining(Long trainingId) {

        trainingController.updateTraining(trainingId, createUpdateData());

    }

    private void printLoggedAdminInMenu() {
        System.out.println("Меню администратора:");
        System.out.println("1. Тренировки пользователей");
        System.out.println("2. Типы тренировок");
        System.out.println("3. Аудит действий пользователей");
        System.out.println("4. Выход из системы");
    }
    private void runAdminMenu() {

        while (true) {
            printLoggedAdminInMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    printHistoryTraining(trainingController.getAllTraining());
                    break;
                case 2:
                    printTrainingTypes();
                    runChangeTrainingTypeMenu();
                    break;
                case 3:
                    auditController.getAllAudits();
                    break;

                case 4:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printChangeTrainingTypesAdmin() {
        System.out.println("1. Добавить тип тренировок");
        System.out.println("2. Вернуться назад");
    }

    public void runChangeTrainingTypeMenu() {
        while (true) {
            printChangeTrainingTypesAdmin();
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    createTrainingType();
                    break;
                case 2:
                    printHistoryTraining(trainingController.getAllTraining());
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void createTrainingType() {
        System.out.println("Добавление нового типа тренировок");
        System.out.println("Тип тренировки");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.nextLine();
        }
        String type = scanner.nextLine();
       trainingController.createTypeTraining(TrainingTypeDTO.builder()
               .type(type)
               .build());
    }
}