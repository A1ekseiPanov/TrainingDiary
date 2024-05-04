package ru.panov.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.panov.util.YamlPropertySourceFactory;

@Configuration
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class DbConfig {
    @Value("${db.classname}")
    private String driverClassName;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.pool.size}")
    private String poolSize;
    @Value("${lb.change_log_file}")
    private String pathChangeLog;
    @Value("${lb.service_schema}")
    private String serviceSchema;

    /**
     * SQL запрос для создания схемы базы данных для служебных талиц liquibase.
     */
    private static final String CREATE_SCHEMA_SQL = "CREATE SCHEMA IF NOT EXISTS liquibase_service";


    @Bean
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(Integer.parseInt(poolSize));
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Конфигурация Liquibase.
     *
     * @return объект SpringLiquibase
     */
    @Bean
    public SpringLiquibase liquibase() {
    jdbcTemplate().execute(CREATE_SCHEMA_SQL);
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(pathChangeLog);
        liquibase.setDataSource(dataSource());
        liquibase.setLiquibaseSchema(serviceSchema);
        return liquibase;
    }



}