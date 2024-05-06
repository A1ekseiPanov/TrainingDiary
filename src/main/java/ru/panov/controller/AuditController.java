package ru.panov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panov.model.Audit;
import ru.panov.service.AuditService;

import java.util.List;

import static ru.panov.util.PathUtil.AUDIT_PATH;

@RestController
@RequestMapping(value = AUDIT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;

    @GetMapping
    public List<Audit> getAll() {
        return auditService.showAllAudits();
    }
}