package ru.panov.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Класс конфигурации для тестирования.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestConfig {
    @Value("${spring.liquibase.change-log}")
    private String pathChangeLog;

    /**
     * Создает и запускает контейнер PostgreSQL для тестов.
     *
     * @param registry Реестр динамических свойств для обновления параметров подключения.
     * @return Контейнер PostgreSQL.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(
            DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.7-alpine");
        registry.add("postgres.driver",
                container::getDriverClassName);
        return container;
    }

    /**
     * Настраивает источник данных HikariCP с использованием параметров контейнера PostgreSQL.
     *
     * @param postgres Контейнер PostgreSQL.
     * @return Источник данных HikariCP.
     */
    @Bean
    public HikariDataSource dataSource(PostgreSQLContainer<?> postgres) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgres.getJdbcUrl());
        config.setUsername(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        return new HikariDataSource(config);
    }

    /**
     * Настраивает Liquibase с использованием источника данных HikariCP и указанного change-log файла.
     *
     * @param dataSource Источник данных HikariCP.
     * @return Экземпляр SpringLiquibase.
     */
    @Bean
    public SpringLiquibase liquibase(HikariDataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(pathChangeLog);
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}