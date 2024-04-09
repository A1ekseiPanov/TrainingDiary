package ru.panov.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class AutoIncrementUtil {
    public static Long increment(Map<Long,?> map){
        return map.keySet().stream()
                .max(Long::compare)
                .orElse(0L) + 1L;
    }
}
