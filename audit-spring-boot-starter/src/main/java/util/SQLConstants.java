package util;

/**
 * Класс с SQL запросами.
 */
public class SQLConstants {
    private SQLConstants() {
    }

    /**
     * SQL запрос для получения всех записей аудита.
     */
    public static final String FIND_ALL_AUDIT =
            "SELECT id, created, class_name, method_name, audit_type, username FROM dbo.audits;";
    /**
     * SQL запрос для создания новой записи аудита.
     */
    public static final String CREATE_AUDIT =
            "INSERT INTO dbo.audits (class_name, method_name, audit_type, username) VALUES (?,?,?,?)";
    /**
     * SQL запрос для поиска записи аудита по идентификатору.
     */
    public static final String FIND_AUDIT_BY_ID =
            "SELECT id, created, class_name, method_name, audit_type, username FROM dbo.audits WHERE id = ?;";

}