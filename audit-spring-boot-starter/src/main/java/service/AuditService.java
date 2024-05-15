package service;


import model.Audit;
import model.AuditType;

import java.util.List;

/**
 * Интерфейс сервиса для работы с аудитом.
 */
public interface AuditService {
    /**
     * Получить список всех записей аудита.
     *
     * @return Список всех записей аудита.
     */
    List<Audit> showAllAudits();

    /**
     * Записать событие аудита.
     *
     * @param className  Имя класса, в котором произошло событие.
     * @param methodName Имя метода, в котором произошло событие.
     * @param auditType  Тип аудита (успешное или неудачное событие).
     * @param username   Имя пользователя, совершившего действие.
     */
    void audit(String className, String methodName, AuditType auditType, String username);
}