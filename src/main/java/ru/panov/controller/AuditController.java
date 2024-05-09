package ru.panov.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panov.model.Audit;
import ru.panov.service.AuditService;

import java.util.List;

import static ru.panov.util.PathConstants.AUDIT_PATH;

/**
 * Контроллер для работы с аудитом.
 */
@RestController
@RequestMapping(value = AUDIT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;

    /**
     * Получение всех записей аудита.
     *
     * @return список записей аудита
     */
    @Operation(
            summary = "Получение всех записей аудита"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Audit> getAll() {
        return auditService.showAllAudits();
    }
}