package ru.panov.controller;


import lombok.RequiredArgsConstructor;
import ru.panov.model.Audit;
import ru.panov.service.AuditService;

import java.util.List;

/**
 * Контроллер для управления операциями аудита.
 */
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;

    /**
     * Получает все записи аудита.
     *
     * @return Список записей аудита.
     */
    public List<Audit> getAllAudits() {
        return auditService.showAllAudits();
    }
}