package ru.panov.in.view;

import lombok.RequiredArgsConstructor;
import ru.panov.controller.TrainingController;
import ru.panov.controller.UserController;
import ru.panov.exception.DuplicateException;
import ru.panov.exception.NotFoundException;
import ru.panov.model.Role;
import ru.panov.model.Training;
import ru.panov.model.TrainingType;
import ru.panov.model.dto.TrainingDTO;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static ru.panov.util.ViewUtil.getUserChoice;

/**
 * Класс UserView представляет пользовательский интерфейс для взаимодействия с функционалом приложения для вошедшего в систему пользователя.
 */
@RequiredArgsConstructor
public class UserView {
    private final UserController userController;
    private final TrainingController trainingController;

    /**
     * Запускает меню для пользователя, вошедшего в систему.
     *
     * @param scanner Объект Scanner для получения ввода пользователя.
     */
    public void runUserMenu(Scanner scanner) {
        while (true) {
            printLoggedInUserMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    createTraining(scanner);
                    break;
                case 2:
                    printHistoryTraining(getAllTraining(), scanner);
                    break;
                case 3:
                    staticTraining(scanner);
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
     * Выводит на экран главное меню пользователя.
     */
    private void printLoggedInUserMenu() {
        System.out.println("Меню пользователя:");
        System.out.println("1. Добавить тренировку");
        System.out.println("2. История тренировок");
        System.out.println("3. Статистика по расходу калорий за выбранный период");
        System.out.println("4. Выход из системы");
    }

    /**
     * Получает все тренировочные.
     *
     * @return Список объектов Training.
     */
    private List<Training> getAllTraining() {
        List<Training> trainings = trainingController.getAllTraining();
        if (trainings.isEmpty()) {
            System.out.println("Тренировок нет");
        }
        return trainings;
    }

    /**
     * Получает данные для создания или обновления тренировки.
     *
     * @param scanner Объект Scanner для получения ввода пользователя.
     * @return Объект TrainingDTO с данными тренировки.
     */
    private TrainingDTO createUpdateData(Scanner scanner) {
        System.out.println("Тип тренировки:");
        List<TrainingType> typesTraining = printTrainingTypes();
        Optional<TrainingType> trainingType;
        while (true) {
            int choice = getUserChoice(scanner);
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

        System.out.println("Время тренировки(чч:мм:сс):");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.next();
        }
        scanner.nextLine();
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

    /**
     * Создает новую тренировку на основе введенных пользователем данных.
     *
     * @param scanner Объект Scanner для получения ввода пользователя.
     */
    private void createTraining(Scanner scanner) {
        try {
            trainingController.createTraining(createUpdateData(scanner));
        } catch (DateTimeParseException e) {
            System.out.println("Не верный ввод времени");
        } catch (NotFoundException | DuplicateException e) {
            System.out.println(e);
        }
    }

    /**
     * Выводит список доступных типов тренировок.
     *
     * @return Список объектов TrainingType.
     */
    public List<TrainingType> printTrainingTypes() {
        List<TrainingType> trainingTypes = trainingController.getAllTrainingType();
        trainingTypes.forEach(type -> System.out.println(type.getId() + ". " + type.getType()));
        return trainingTypes;
    }

    /**
     * Выводит историю тренировок.
     *
     * @param trainings Список объектов Training, представляющих историю тренировок.
     * @param scanner   Объект Scanner для получения ввода пользователя.
     */
    public void printHistoryTraining(List<Training> trainings, Scanner scanner) {
        if (trainings.isEmpty()) {
            return;
        }
        trainings.forEach(this::printTraining);
        if (userController.loggedUser().getRole().equals(Role.USER)) {
            runTrainingMenu(scanner);
        }
    }

    /**
     * Выводит статистику по расходу калорий за выбранный период.
     *
     * @param scanner Объект Scanner для получения ввода пользователя.
     */
    private void staticTraining(Scanner scanner) {
        System.out.println("Укажите периодд за каоторый необходима статистика");
        System.out.println("Дата и время начальное(дд-мм-гггг чч:мм)");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.nextLine();
        }
        scanner.nextLine();
        String start = scanner.nextLine();
        System.out.println("Дата и время конечные(дд-мм-гггг чч:мм)");
        while (!scanner.hasNextLine()) {
            System.out.println("Неверный ввод.");
            scanner.nextLine();
        }
        String end = scanner.nextLine();
        try {
            System.out.println("Потрачено калорий: " + trainingController.caloriesSpentOverPeriod(start, end));
        } catch (DateTimeParseException e) {
            System.out.println("Не верный ввод данных");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Выводит информацию о тренировке.
     *
     * @param training Объект Training, представляющий тренировку.
     */
    private void printTraining(Training training) {
        System.out.println("Id: " + training.getId());
        System.out.println("Тип тренировки: " + trainingController.findTrainingTypeById(training.getTypeId()).getType());
        System.out.println("Потрачено калорий: " + training.getCountCalories());
        System.out.println("Время тренировки: " + training.getTrainingTime());
        System.out.println("Дополнительная информация: " + training.getAdditionalInfo());
        System.out.println("Дата тренировки: " + training.getCreated());
        System.out.println("Дата последнего редактирования: " + training.getUpdated());
        System.out.println("-------------");
    }


    private void printAddedTrainingUserMenu() {
        System.out.println("1. Просмотр тренировки");
        System.out.println("2. Вернуться назад");
    }

    /**
     * Запускает меню для выбора тренировки.
     *
     * @param scanner Объект Scanner для получения ввода пользователя.
     */
    private void runTrainingMenu(Scanner scanner) {
        while (true) {
            printAddedTrainingUserMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    choosingTraining(scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    /**
     * Позволяет пользователю выбрать тренировку по id.
     *
     * @param scanner Объект Scanner для получения ввода пользователя.
     */
    private void choosingTraining(Scanner scanner) {
        System.out.println("Выбирите тренировку по id");
        int choice = getUserChoice(scanner);
        try {
            Training training = trainingController.getByTrainingById((long) choice);
            printTraining(training);
            runChangeTrainingMenu(training.getId(), scanner);
        } catch (NotFoundException e) {
            System.out.println(e);
        }
    }

    /**
     * Выводит меню для изменения или удаления тренировки.
     */
    private void printManipulationTrainingUserMenu() {
        System.out.println("1. Редактировать тренировку");
        System.out.println("2. Удалить тренировку");
        System.out.println("3. Вернуться назад");
    }

    /**
     * Запускает меню для изменения или удаления тренировки.
     *
     * @param trainingId ID тренировки, над которой нужно выполнить действия.
     * @param scanner    Объект Scanner для получения ввода пользователя.
     */
    private void runChangeTrainingMenu(Long trainingId, Scanner scanner) {
        while (true) {
            printManipulationTrainingUserMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    updatingTraining(trainingId, scanner);
                    return;
                case 2:
                    deleteTraining(trainingId);
                    return;
                case 3:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    /**
     * Удаляет тренировку по указанному ID.
     *
     * @param trainingId ID тренировки, которую необходимо удалить.
     */
    private void deleteTraining(Long trainingId) {
        try {
            trainingController.deleteTraining(trainingId);
        } catch (NotFoundException e) {
            System.out.println(e);
        }
    }

    /**
     * Обновляет информацию о тренировке с указанным ID.
     *
     * @param trainingId ID тренировки, которую необходимо обновить.
     * @param scanner    Объект Scanner для получения ввода пользователя.
     */
    private void updatingTraining(Long trainingId, Scanner scanner) {
        try {
            trainingController.updateTraining(trainingId, createUpdateData(scanner));
        } catch (DateTimeParseException e) {
            System.out.println("Не верный ввод времени");
        } catch (NotFoundException e) {
            System.out.println(e);
        }
    }
}