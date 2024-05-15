package aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import util.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * Аспект для логирования выполнения методов.
 */
@Aspect
@Slf4j
public class LoggableAspect {

    /**
     * Точка среза для любого метода.
     */
    @Pointcut("execution(* ru.panov..*(..))")
    public void anyMethod() {
    }

    /**
     * Точка среза для GenericFilterBean.
     */
    @Pointcut("this(org.springframework.web.filter.GenericFilterBean)")
    public void genericFilterBeenImpl() {
    }

    /**
     * Аспект, выполняющий логирование выполнения методов.
     *
     * @param proceedingJoinPoint точка соединения выполнения метода
     * @return результат выполнения метода
     */
    @Around("anyMethod() && !genericFilterBeenImpl()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        log.info("Execution of method: ({}) {} finished. Execution time is {} ms.",
                DateTimeUtil.parseDateTime(LocalDateTime.now()),
                proceedingJoinPoint.getSignature(),
                end);
        return result;
    }
}