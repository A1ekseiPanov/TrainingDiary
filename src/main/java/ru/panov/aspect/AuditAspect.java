package ru.panov.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.panov.model.AuditType;
import ru.panov.service.AuditService;
import ru.panov.util.PathConstants;

/**
 * Аспект для аудита выполнения методов, помеченных аннотацией @Audit.
 */
@Aspect
@Component
@RequiredArgsConstructor
@RequestMapping(PathConstants.AUTH_PATH)
public class AuditAspect {
    private final AuditService service;

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * Точка среза для методов, помеченных аннотацией @Audit.
     */
    @Pointcut("(within(@ru.panov.annotations.Audit *) || execution(@ru.panov.annotations.Audit * *(..))) && execution(* *(..))")
    public void annotatedByAudit() {
    }

    /**
     * Логирование успешного выполнения метода помеченного аннотацией @Audit.
     *
     * @param joinPoint точка соединения
     */
    @AfterReturning(pointcut = "annotatedByAudit()")
    public void auditMethodSuccess(JoinPoint joinPoint) {
        service.audit(joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                AuditType.SUCCESS, getUsername());
    }

    /**
     * Логирование исключения в методе помеченного аннотацией @Audit.
     *
     * @param joinPoint точка соединения
     * @param ex        исключение
     */
    @AfterThrowing(pointcut = "annotatedByAudit()", throwing = "ex")
    public void auditMethodException(JoinPoint joinPoint, Throwable ex) {
        service.audit(joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                AuditType.FAIL, getUsername());
    }
}