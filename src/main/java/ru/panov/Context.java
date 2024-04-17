package ru.panov;

import ru.panov.in.view.factory.ViewFactory;
import ru.panov.util.ConnectionUtil;

import java.util.Scanner;

import static ru.panov.util.ViewUtil.getUserChoice;

/**
 * Класс Context представляет основной контекст приложения.
 * Он содержит методы для запуска приложения.
 */
public class Context {
    public Context() {
    }

    private final Scanner scanner = new Scanner(System.in);

    private void printMainMenu() {
        System.out.println("Training Diary Menu:");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход в систему");
        System.out.println("3. Выход");
    }

    /**
     * Запускает приложение и обрабатывает основной цикл.
     */
    public void run() {
        while (true) {
            printMainMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1 -> ViewFactory.getInstance().getAuthView().registerUser(scanner);
                case 2 -> ViewFactory.getInstance().getAuthView().login(scanner);
                case 3 -> {
                    System.out.println("Выход из приложения. До свидания!");
                    ConnectionUtil.closePool();
                    System.exit(0);
                }
                default -> System.out.println("Такого пункта меню не существует. Пожалуйста, попробуйте снова.");
            }
        }
    }
}