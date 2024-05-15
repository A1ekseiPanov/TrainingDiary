package conf;

import aspect.AuditAspect;
import dao.AuditDAO;
import dao.impl.jdbc.JdbcAuditDAOImpl;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import service.AuditService;
import service.AuditServiceImpl;

@Slf4j
@Configuration
public class AuditAutoConfiguration {
    @Bean
    public AuditDAO auditDAO(JdbcTemplate jdbcTemplate) {
        return new JdbcAuditDAOImpl(jdbcTemplate);
    }

    @Bean
    public AuditService auditService(JdbcTemplate jdbcTemplate) {
        return new AuditServiceImpl(auditDAO(jdbcTemplate));
    }

    @Bean
    public AuditAspect aspect(JdbcTemplate jdbcTemplate) {
        return new AuditAspect(auditService(jdbcTemplate));
    }

    @PostConstruct
    public void init() {
        log.info("AuditAutoConfiguration init");
    }
}