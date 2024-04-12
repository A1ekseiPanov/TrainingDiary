package ru.panov.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Утилитный класс для автоматического инкремента числовых идентификаторов.
 */
@UtilityClass
public class AutoIncrementUtil {
    /**
     * Метод для автоматического инкремента числовых идентификаторов.
     *
     * @param map Мапа, содержащая числовые идентификаторы в качестве ключей.
     * @return Следующее доступное значение числового идентификатора.
     */
    public static Long increment(Map<Long, ?> map) {
        return map.keySet().stream()
                .max(Long::compare)
                .orElse(0L) + 1L;
    }
}