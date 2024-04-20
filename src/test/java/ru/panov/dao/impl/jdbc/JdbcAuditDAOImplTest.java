package ru.panov.dao.impl.jdbc;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.panov.dao.AuditDAO;
import ru.panov.model.Audit;
import ru.panov.model.AuditType;
import ru.panov.util.LiquibaseUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcAuditDAOImplTest {
    private static AuditDAO auditDAO;
    private static Connection connection;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:14.7-alpine");

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgres.start();
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        auditDAO = new JdbcAuditDAOImpl(connection);
        LiquibaseUtil.update(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        postgres.stop();
        connection.close();
    }

    @Test
    @DisplayName("Получение записи аудита по её id, запись найдена")
    void findById_ExistingIdReturnsAudit() {
        Long id = 1L;

        Optional<Audit> audit = auditDAO.findById(id);

        assertThat(audit).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "user")
                .hasFieldOrPropertyWithValue("methodName","save")
                .hasFieldOrPropertyWithValue("className", "aaaa");
    }

    @Test
    @DisplayName("Получение всех записей аудита, записи есть")
    void findAll_ReturnsAllAudits() {
        List<Audit> auditList = auditDAO.findAll();

        assertThat(auditList).isNotEmpty();
        assertThat(auditList.size()).isEqualTo(1);
    }

    @Test
    @Order(1)
    @DisplayName("Сохранение новой записи аудита, записть сохранена")
    void save_NewAuditReturnsSavedAudit() {
        Audit audit = Audit.builder()
                .type(AuditType.SUCCESS)
                .username("user")
                .methodName("save")
                .className("aaaa")
                .build();

        Audit savedAudit = auditDAO.save(audit);

        assertThat(savedAudit)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("username", audit.getUsername())
                .hasFieldOrPropertyWithValue("methodName", audit.getMethodName())
                .hasFieldOrPropertyWithValue("className", audit.getClassName());
    }
}