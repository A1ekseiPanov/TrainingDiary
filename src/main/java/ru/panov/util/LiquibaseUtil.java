package ru.panov.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static ru.panov.util.PropertiesUtil.*;

/**
 * Утилитный класс для работы с Liquibase - инструментом для управления миграциями базы данных.
 */
public final class LiquibaseUtil {
    private static final String CHANGELOG_FILE_PATH_KEY = "lb.change_log_file";
    private static final String SERVICE_SCHEMA_KEY = "lb.service_schema";
    /**
     * SQL запрос для создания схемы базы данных для служебных талиц liquibase.
     */
    private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS liquibase_service";

    private LiquibaseUtil() {
    }

    /**
     * Обновляет базу данных с использованием миграций из указанного changelog-файла.
     *
     * @param connection Соединение с базой данных.
     */
    public static void update(Connection connection) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SCHEMA)) {
            preparedStatement.execute();
            Database database = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(get(SERVICE_SCHEMA_KEY));
            Liquibase liquibase = new Liquibase(get(CHANGELOG_FILE_PATH_KEY),
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            System.out.println("Миграции выполнены успешно");
        } catch (LiquibaseException |SQLException e) {
            throw new RuntimeException(e);
        }
    }
}