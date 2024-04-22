package ru.panov.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилитный класс для работы с файлом свойств.
 */
public final class PropertiesUtil {
    public static final Properties PROPERTIES = new Properties();

    private PropertiesUtil() {
    }

    static {
        loadProperties();
    }

    /**
     * Возвращает значение свойства по ключу.
     *
     * @param key Ключ свойства
     * @return Значение свойства
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Загружает свойства из файла "application.properties" в объект Properties.
     */
    private static void loadProperties() {
        try (InputStream inputStream = PropertiesUtil.class
                .getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}