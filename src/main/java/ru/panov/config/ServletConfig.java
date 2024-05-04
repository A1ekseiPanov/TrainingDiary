package ru.panov.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.panov.util.ConnectionUtil;
import ru.panov.util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class ServletConfig {
    /**
     * Конфигурация сервлетов, выполняющаяся при запуске приложения.
     */
    @WebListener
    public static class ServletsConfig implements ServletContextListener {
        @Override
        public void contextInitialized(ServletContextEvent sce) {
            try (Connection connection = ConnectionUtil.get()) {
                LiquibaseUtil.update(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
