package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import ru.panov.model.Audit;
import ru.panov.service.AuditService;

import java.util.List;

@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;
    public List<Audit> getAllAudits() {
        return auditService.showAllAudits();
    }

}
