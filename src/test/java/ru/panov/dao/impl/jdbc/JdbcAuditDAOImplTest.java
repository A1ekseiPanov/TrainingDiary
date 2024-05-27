package ru.panov.dao.impl.jdbc;

import dao.AuditDAO;
import model.Audit;
import model.AuditType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.panov.config.TestConfig;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestConfig.class})
@Transactional
class JdbcAuditDAOImplTest {
    @Autowired
    private AuditDAO auditDAO;

    @Test
    @DisplayName("Получение записи аудита по её id, запись найдена")
    void findById_ExistingIdReturnsAudit() {
        Long id = 1L;

        Optional<Audit> audit = auditDAO.findById(id);

        assertThat(audit).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "user")
                .hasFieldOrPropertyWithValue("methodName", "save")
                .hasFieldOrPropertyWithValue("className", "Service");
    }

    @Test
    @DisplayName("Получение всех записей аудита, записи есть")
    void findAll_ReturnsAllAudits() {
        List<Audit> auditList = auditDAO.findAll();

        assertThat(auditList).isNotEmpty();
        assertThat(auditList.size()).isEqualTo(1);
    }

    @Test
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