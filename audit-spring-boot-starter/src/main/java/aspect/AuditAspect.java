package aspect;

import lombok.RequiredArgsConstructor;
import model.AuditType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import service.AuditService;


/**
 * Аспект для аудита выполнения методов, помеченных аннотацией @Audit.
 */
@Aspect
//@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService service;

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * Точка среза для методов, помеченных аннотацией @Audit.
     */
    @Pointcut("within(@annotations.Audit *) && execution(* * (..))")
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