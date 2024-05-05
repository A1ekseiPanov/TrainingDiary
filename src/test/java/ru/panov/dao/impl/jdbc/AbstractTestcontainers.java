//package ru.panov.dao.impl.jdbc;
//
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public abstract class AbstractTestcontainers {
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            DockerImageName.parse("postgres:14.7-alpine"));
//
//    static {
//        postgres.start();
//        try {
//            LiquibaseUtil.update(getConnection());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(
//                postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword());
//    }
//}