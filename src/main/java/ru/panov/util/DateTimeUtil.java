package ru.panov.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private DateTimeUtil() {
    }

    /**
     * Форматтер для даты и времени.
     */
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    /**
     * Форматтер для времени.
     */
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Преобразует LocalDateTime в строку с заданным форматом.
     *
     * @param dateTime LocalDateTime для преобразования.
     * @return Строка, содержащая дату и время в заданном формате.
     */
    public static String parseDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    /**
     * Парсит строку в LocalDateTime с заданным форматом.
     *
     * @param dateTimeString Строка, содержащая дату и время.
     * @return Объект LocalDateTime.
     */
    public static LocalDateTime parseDateTimeFromString(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    /**
     * Преобразует LocalTime в строку с заданным форматом.
     *
     * @param time LocalTime для преобразования.
     * @return Строка, содержащая время в заданном формате.
     */
    public static String parseTime(LocalTime time) {
        return time.format(timeFormatter);
    }

    /**
     * Парсит строку в LocalTime с заданным форматом.
     *
     * @param timeString Строка, содержащая время.
     * @return Объект LocalTime.
     */
    public static LocalTime parseTimeFromString(String timeString) {
        return LocalTime.parse(timeString, timeFormatter);
    }
}