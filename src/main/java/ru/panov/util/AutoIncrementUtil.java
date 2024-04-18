package ru.panov.util;

import java.util.Map;

public final class AutoIncrementUtil {
    private AutoIncrementUtil() {
    }

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