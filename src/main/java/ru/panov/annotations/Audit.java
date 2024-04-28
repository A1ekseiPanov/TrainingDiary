package ru.panov.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, используемая для пометки методов или классов, которые должны быть отслеживаемыми аудитом.
 * Информация, помеченная этой аннотацией, может быть использована для регистрации событий выполнения методов
 * или операций с помощью системы аудита.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Audit {
    String username() default "";
}