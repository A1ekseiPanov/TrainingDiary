package service;

import dao.AuditDAO;
import lombok.RequiredArgsConstructor;
import model.Audit;
import model.AuditType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса для работы с аудитом.
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditDAO auditDAO;

    @Override
    public List<Audit> showAllAudits() {
        return auditDAO.findAll();
    }

    @Override
    public void audit(String classname, String methodName, AuditType auditType, String username) {
        auditDAO.save(Audit.builder()
                .className(classname)
                .methodName(methodName)
                .type(auditType)
                .username(username)
                .build());
    }
}