package ru.panov.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final ObjectNode OBJECT_NODE = OBJECT_MAPPER.createObjectNode();

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        ObjectReader reader = OBJECT_MAPPER.readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> String writeValue(T obj) {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + obj + "'", e);
        }
    }

    /**
     * Выводит сообщение в формате JSON и устанавливает статус ответа.
     *
     * @param fieldName имя поля сообщения
     * @param message   текст сообщения
     * @param status    статус ответа
     * @param resp      HTTP ответ
     * @throws IOException если произошла ошибка ввода-вывода
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
}