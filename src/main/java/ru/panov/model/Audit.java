package ru.panov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {
    private Long id;
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();
    private String className;
    private String methodName;
    private AuditType auditType;
    private String username;
}
