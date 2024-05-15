package ru.panov.dao.impl.jdbc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * ???????????? ??? ??????????????? ???????????? ? ?????????????? ???? ?????? PostgreSQL
 */
@TestConfiguration(proxyBeanMethods = false)
@ComponentScan(basePackages = "ru.panov")
public class TestJdbcConfig  {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.7-alpine");
        registry.add("postgres.driver",
                postgres::getDriverClassName);
        return postgres;
    }
//    @Container
//    @ServiceConnection
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            DockerImageName.parse("postgres:14.7-alpine"));
//


//    /**
//     * ??????? ? ??????????? ???????? ?????? HikariCP ??? ??????.
//     *
//     * @return ???????? ?????? HikariCP
//     */
//    @Bean
//    public HikariDataSource dataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(postgres.getJdbcUrl());
//        config.setUsername(postgres.getUsername());
//        config.setPassword(postgres.getPassword());
//        return new HikariDataSource(config);
//    }

//    /**
//     * ??????? ? ??????????? ?????? JdbcTemplate ??? ??????.
//     *
//     * @return ?????? JdbcTemplate
//     */
//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(dataSource());
//    }
//
//    @Bean
//    public AuditDAO auditDAO() {
//        return new JdbcAuditDAOImpl(jdbcTemplate());
//    }
//
//    @Bean
//    public TrainingDAO trainingDAO() {
//        return new JdbcTrainingDAOImpl(jdbcTemplate());
//    }
//
//    @Bean
//    public TrainingTypeDAO typeDAO() {
//        return new JdbcTrainingTypeDAOImpl(jdbcTemplate());
//    }
//
//    @Bean
//    public UserDAO userDAO() {
//        return new JdbcUserDAOImpl(jdbcTemplate());
//    }
//
//    /**
//     * ???????????? Liquibase ??? ??????.
//     *
//     * @return ?????? SpringLiquibase
//     */
//    @Bean
//    public SpringLiquibase liquibase() {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setChangeLog(pathChangeLog);
//        liquibase.setDataSource(dataSource());
//        return liquibase;
//    }

//    /**
//     * ????????? ????????? ??????????.
//     *
//     * @return ???????? ??????????
//     */
//    @Override
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }
}