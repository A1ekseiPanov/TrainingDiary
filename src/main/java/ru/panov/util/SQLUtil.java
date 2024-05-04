package ru.panov.util;

public class SQLUtil {
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
    /**
     * SQL запрос для получения всех типов тренировок.
     */
    public static final String FIND_ALL_TRAINING_TYPE =
            "SELECT id, type, created FROM dbo.training_types";
    /**
     * SQL запрос для создания нового типа тренировки.
     */
    public static final String CREATE_TRAINING_TYPE =
            "INSERT INTO dbo.training_types (type) VALUES (?)";
    /**
     * SQL запрос для поиска типа тренировки по его ID.
     */
    public static final String FIND_TRAINING_TYPE_BY_ID =
            "SELECT id, type, created FROM dbo.training_types WHERE id = ?";
    /**
     * SQL запрос для поиска типа тренировки по его названию.
     */
    public static final String FIND_TRAINING_TYPE_BY_TYPE =
            "SELECT id, type, created FROM dbo.training_types WHERE type = ?";
    /**
     * SQL запрос для поиска пользователя по его ID.
     */
    public static final String FIND_USER_BY_ID =
            "SELECT id, username, password, role, created FROM dbo.users WHERE id = ?";
    /**
     * SQL запрос для получения всех пользователей.
     */
    public static final String FIND_ALL_USERS =
            "SELECT id, username, password, role, created FROM dbo.users;";
    /**
     * SQL запрос для поиска пользователя по его имени пользователя.
     */
    public static final String FIND_USER_BY_USERNAME =
            "SELECT id, username, password, role, created FROM dbo.users WHERE username = ?";
    /**
     * SQL запрос для сохранения нового пользователя.
     */
    public static final String SAVE_USER =
            "INSERT INTO dbo.users (username, password, role, created) VALUES (?,?,?,?)";
    /**
     * SQL запрос для поиска тренировки по её ID и ID пользователя.
     */
    public static final String FIND_TRAINING_BY_ID_AND_USER_ID = """
            SELECT id, type_id, created, updated, count_calories, training_time, additional_info, user_id
            FROM dbo.trainings
            WHERE id = ?
            AND user_id = ?
            """;
    /**
     * SQL запрос для получения всех тренировок.
     */
    public static final String FIND_ALL_TRAINING = """
            SELECT id, type_id, created, updated, count_calories, training_time, additional_info, user_id
            FROM dbo.trainings
            ORDER BY created
            """;
    /**
     * SQL запрос для получения всех тренировок с использованием ограничения и смещения.
     */
    public static final String FIND_ALL_TRAINING_LIMIT_OFFSET = FIND_ALL_TRAINING + """
            LIMIT ?
            OFFSET ?
            """;
    /**
     * SQL запрос для получения всех тренировок для указанного пользователя.
     */
    public static final String FIND_ALL_TRAINING_BY_USER_ID = """
            SELECT id, type_id, created, updated, count_calories, training_time, additional_info, user_id
            FROM dbo.trainings
            WHERE user_id = ?
            ORDER BY created
            """;
    /**
     * SQL запрос для создания новой тренировки.
     */
    public static final String CREATE_TRAINING = """
            INSERT INTO dbo.trainings  (type_id, created, updated, count_calories, training_time, additional_info, user_id)
            VALUES (?,?,?,?,?,?,?)
            """;
    /**
     * SQL запрос для обновления существующей тренировки.
     */
    public static final String UPDATE_TRAINING = """
            UPDATE dbo.trainings 
            set type_id = ?,
                created = ?,
                updated = ?,
                count_calories = ?,
                training_time = ?,
                additional_info = ?
            WHERE id = ?
            AND user_id = ?
            """;
    /**
     * SQL запрос для удаления тренировки по её ID и ID пользователя.
     */
    public static final String DELETE_TRAINING = """
            DELETE FROM dbo.trainings
            WHERE id = ?
            AND user_id = ?
            """;
    /**
     * SQL запрос для вычисления суммы сожженных калорий за указанный период.
     */
    public static final String SUM_CALORIES_SPENT_OVER_PERIOD = """
            SELECT SUM(count_calories) FROM dbo.trainings
            WHERE user_id = ? AND created BETWEEN ? AND ?
            """;
}
