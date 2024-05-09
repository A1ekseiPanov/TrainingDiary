package ru.panov.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Инициализирует безопасность веб-приложения.
 * Регистрирует DelegatingFilterProxy для использования springSecurityFilterChain
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
}