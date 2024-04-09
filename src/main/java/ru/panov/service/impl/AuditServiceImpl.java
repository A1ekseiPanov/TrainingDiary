package ru.panov.service.impl;

import lombok.RequiredArgsConstructor;
import ru.panov.dao.AuditDAO;
import ru.panov.model.Audit;
import ru.panov.model.AuditType;
import ru.panov.service.AuditService;

import java.util.List;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditDAO auditDAO;

    @Override
    public List<Audit> showAllAudits() {
        return auditDAO.findAll();
    }

    @Override
    public void audit(AuditType auditType, String message) {
        auditDAO.save(Audit.builder()
                .className(Class.class.getSimpleName())
                .methodName(Class.class.getEnclosingMethod().getName())
                .auditType(auditType)
                .message(message)
                .build());
    }
}