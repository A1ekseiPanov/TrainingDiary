package ru.panov.service;

import ru.panov.model.Audit;
import ru.panov.model.AuditType;

import java.util.List;

public interface AuditService {
    List<Audit> showAllAudits();

    void audit(AuditType auditType, String message);
}