package ru.panov.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public String parseDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    public LocalDateTime parseDateTimeFromString(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    public String parseTime(LocalTime time) {
        return time.format(timeFormatter);
    }

    public LocalTime parseTimeFromString(String timeString) {
        return LocalTime.parse(timeString, timeFormatter);
    }
}
