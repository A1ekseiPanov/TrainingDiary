package util;

import annotations.EnableLogg;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Условие для включения автоконфигурации на основе наличия бина с аннотацией @EnableLogg.
 */
public class EnableMyStarterCondition implements Condition {

    /**
     * Проверяет наличие бинов с аннотацией @EnableLogg в контексте приложения.
     *
     * @param context  Контекст условия.
     * @param metadata Метаданные типа с аннотацией.
     * @return true, если есть бины с аннотацией @EnableLogg, в противном случае - false.
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getBeanFactory().getBeansWithAnnotation(EnableLogg.class).size() > 0;
    }
}