package ru.panov.util;

/**
 * Класс с константами для работы с путями URL.
 */
public class PathConstants {
    private PathConstants() {
    }

    /**
     * Путь к эндпоинту аудита.
     */
    public static final String AUDIT_PATH = "/audit";
    public static final String AUTH_PATH = "/auth";

    /**
     * Путь к эндпоинту входа в систему.
     */
    public static final String LOGIN_PATH = "/login";

    /**
     * Путь к эндпоинту регистрации пользователя.
     */
    public static final String REGISTRATION_PATH = "/registration";

    /**
     * Путь к эндпоинту управления тренировками.
     */
    public static final String TRAINING_PATH = "/trainings";

    /**
     * Путь к эндпоинту для подсчёта сожженных калорий в тренировках.
     */
    public static final String TRAINING_BURNED_CALORIES_PATH = "/calories";

    /**
     * Путь к эндпоинту управления типами тренировок.
     */
    public static final String TRAINING_TYPE_PATH = "/types";
}