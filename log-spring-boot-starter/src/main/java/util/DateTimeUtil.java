package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    public static String parseDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(CUSTOM_FORMATTER);
    }
}