package ru.panov.dao.impl.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.panov.dao.AuditDAO;
import ru.panov.dao.TrainingDAO;
import ru.panov.util.YamlPropertySourceFactory;

@Configuration
@Testcontainers
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class TestConfig implements TransactionManagementConfigurer {

    @Value("${lb.change_log_file}")
    private String pathChangeLog;
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14.7-alpine"));


    static {
        postgres.start();
    }

    @Bean
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgres.getJdbcUrl());
        config.setUsername(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public AuditDAO auditDAO() {
        return new JdbcAuditDAOImpl(jdbcTemplate());
    }

    @Bean
    public TrainingDAO trainingDAO(){
        return new JdbcTrainingDAOImpl(jdbcTemplate());
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(pathChangeLog);
        liquibase.setDataSource(dataSource());
        return liquibase;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
