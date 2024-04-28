package ru.panov.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Утилитный класс для работы с JSON.
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final ObjectNode OBJECT_NODE = OBJECT_MAPPER.createObjectNode();

    /**
     * Читает JSON-строку и преобразует её в объект указанного класса.
     *
     * @param json  JSON-строка
     * @param clazz класс объекта
     * @param <T>   тип объекта
     * @return объект указанного класса
     */
    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    /**
     * Преобразует объект в JSON-строку.
     *
     * @param obj объект для преобразования
     * @param <T> тип объекта
     * @return JSON-строка
     */
    public static <T> String writeValue(T obj) {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + obj + "'", e);
        }
    }

    /**
     * Выводит сообщение в формате JSON и устанавливает указанный HTTP-статус.
     *
     * @param fieldName имя поля
     * @param message   сообщение
     * @param status    HTTP-статус
     * @param resp      HTTP-ответ
     * @throws IOException в случае ошибки ввода/вывода
     */
    public static void printMessage(String fieldName,
                                    String message,
                                    int status,
                                    HttpServletResponse resp) throws IOException {
        OBJECT_NODE.removeAll();
        OBJECT_NODE.put(fieldName, message);
        resp.getWriter().write(writeValue(OBJECT_NODE));
        resp.setStatus(status);
    }

    /**
     * Читает JSON-строку из HTTP-запроса.
     *
     * @param req HTTP-запрос
     * @return JSON-строка
     * @throws IOException в случае ошибки ввода/вывода
     */
    public static String readJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader();
             Stream<String> lines = reader.lines()) {
            lines.forEach(sb::append);
        }
        return sb.toString();
    }
}