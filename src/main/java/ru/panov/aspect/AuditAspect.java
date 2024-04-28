package ru.panov.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.panov.annotations.Audit;
import ru.panov.model.AuditType;
import ru.panov.service.AuditService;
import ru.panov.service.factory.ServiceFactory;


/**
 * Аспект для аудита выполнения методов, помеченных аннотацией @Audit.
 */
@Aspect
public class AuditAspect {
    private final AuditService service;

    public AuditAspect() {
        this.service = ServiceFactory.getInstance().getAuditService();
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
    public void auditMethodSuccess(ProceedingJoinPoint  joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Audit audit = methodSignature.getMethod().getAnnotation(Audit.class);

        String payload = audit.username();
        service.audit(joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                AuditType.SUCCESS, payload);
    }

    /**
     * Логирование исключения в методе помеченного аннотацией @Audit.
     *
     * @param joinPoint точка соединения
     * @param ex        исключение
     */
    @AfterThrowing(pointcut = "annotatedByAudit()", throwing = "ex")
    public void auditMethodException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Audit audit = methodSignature.getMethod().getAnnotation(Audit.class);

        String payload = audit.username();
        service.audit(joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                AuditType.FAIL, payload);;
    }
}