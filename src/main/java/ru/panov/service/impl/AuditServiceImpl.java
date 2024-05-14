package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.panov.dao.AuditDAO;
import ru.panov.model.Audit;
import ru.panov.model.AuditType;
import ru.panov.service.AuditService;

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