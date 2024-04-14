package ru.panov.util;

import lombok.experimental.UtilityClass;

import java.util.Scanner;

/**
 * Утилитный класс для работы с пользовательским интерфейсом.
 */
@UtilityClass
public class ViewUtil {

    /**
     * Выбор пункта меню.
     *
     * @param scanner Объект Scanner для считывания ввода пользователя.
     * @return Выбор пользователя.
     */
    public static int getUserChoice(Scanner scanner) {
        System.out.print("Выберете пункт из меню: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
