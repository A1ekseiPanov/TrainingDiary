package ru.panov.dao.impl.jdbc;


import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingDAO;
import ru.panov.exception.DaoException;
import ru.panov.model.Training;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса TrainingDAO, использующая JDBC для взаимодействия с базой данных.
 */
@RequiredArgsConstructor
public class JdbcTrainingDAOImpl implements TrainingDAO {
    private final Connection connection;
    /**
     * SQL запрос для поиска тренировки по её ID и ID пользователя.
     */
    private static final String FIND_TRAINING_BY_ID_AND_USER_ID = """
            SELECT id, type_id, created, updated, count_calories, training_time, additional_info, user_id
            FROM dbo.trainings
            WHERE id = ?
            AND user_id = ?
            """;
    /**
     * SQL запрос для получения всех тренировок.
     */
    private static final String FIND_ALL_TRAINING = """
            SELECT id, type_id, created, updated, count_calories, training_time, additional_info, user_id
            FROM dbo.trainings
            """;
    /**
     * SQL запрос для получения всех тренировок с использованием ограничения и смещения.
     */
    private static final String FIND_ALL_TRAINING_LIMIT_OFFSET = FIND_ALL_TRAINING + """
            LIMIT ?
            OFFSET ?
            """;
    /**
     * SQL запрос для получения всех тренировок для указанного пользователя.
     */
    private static final String FIND_ALL_TRAINING_BY_USER_ID = FIND_ALL_TRAINING + """
            WHERE user_id = ?
            """;
    /**
     * SQL запрос для создания новой тренировки.
     */
    private static final String CREATE_TRAINING = """
            INSERT INTO dbo.trainings  (type_id, created, updated, count_calories, training_time, additional_info, user_id)
            VALUES (?,?,?,?,?,?,?)
            """;
    /**
     * SQL запрос для обновления существующей тренировки.
     */
    private static final String UPDATE_TRAINING = """
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
    private static final String DELETE_TRAINING = """
            DELETE FROM dbo.trainings
            WHERE id = ?
            AND user_id = ?
            """;
    /**
     * SQL запрос для вычисления суммы сожженных калорий за указанный период.
     */
    private static final String SUM_CALORIES_SPENT_OVER_PERIOD = """
            SELECT SUM(count_calories) FROM dbo.trainings
            WHERE user_id = ? AND created BETWEEN ? AND ?
            """;

    @Override
    public Optional<Training> findById(Long id, Long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_TRAINING_BY_ID_AND_USER_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            Training training = null;
            if (resultSet.next()) {
                training = trainingBuilder(resultSet);
            }
            return Optional.ofNullable(training);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Training> findAll() {
        List<Training> trainings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TRAINING)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trainings.add(trainingBuilder(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Collections.unmodifiableList(trainings);
    }

    @Override
    public Training save(Training entity, Long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TRAINING, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, entity.getTypeId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getCreated()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getUpdated()));
            preparedStatement.setDouble(4, entity.getCountCalories());
            preparedStatement.setTime(5, Time.valueOf(entity.getTrainingTime()));
            preparedStatement.setString(6, entity.getAdditionalInfo());
            preparedStatement.setLong(7, userId);
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            if (key.next()) {
                entity.setId(key.getLong("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Training update(Training entity, Long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRAINING)) {
            preparedStatement.setLong(1, entity.getTypeId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getCreated()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getUpdated()));
            preparedStatement.setDouble(4, entity.getCountCalories());
            preparedStatement.setTime(5, Time.valueOf(entity.getTrainingTime()));
            preparedStatement.setString(6, entity.getAdditionalInfo());
            preparedStatement.setLong(7, entity.getId());
            preparedStatement.setLong(8, userId);
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            if (key.next()) {
                entity.setId(key.getLong("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id, Long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRAINING)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Double caloriesSpentOverPeriod(LocalDateTime start, LocalDateTime end, Long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SUM_CALORIES_SPENT_OVER_PERIOD)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(end));

            ResultSet resultSet = preparedStatement.executeQuery();
            Double calories = null;
            if (resultSet.next()) {
                calories = resultSet.getDouble(1);
            }
            return calories;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Training> findAllByUserId(Long userId) {
        List<Training> trainings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TRAINING_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trainings.add(trainingBuilder(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Collections.unmodifiableList(trainings);
    }

    @Override
    public List<Training> findAll(int limit, int offset) {
        List<Training> trainings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TRAINING_LIMIT_OFFSET)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trainings.add(trainingBuilder(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Collections.unmodifiableList(trainings);
    }

    private Training trainingBuilder(ResultSet resultSet) throws SQLException {
        Training training;
        training = Training.builder()
                .id(resultSet.getLong("id"))
                .typeId(resultSet.getLong("type_id"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                .countCalories(resultSet.getDouble("count_calories"))
                .trainingTime(resultSet.getTime("training_time").toLocalTime())
                .additionalInfo(resultSet.getString("additional_info"))
                .userId(resultSet.getLong("user_id"))
                .build();
        return training;
    }
}