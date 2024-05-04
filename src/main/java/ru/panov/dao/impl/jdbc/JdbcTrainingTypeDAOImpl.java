package ru.panov.dao.impl.jdbc;


import lombok.RequiredArgsConstructor;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.exception.DaoException;
import ru.panov.model.TrainingType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.panov.util.SQLUtil.*;

/**
 * Реализация интерфейса TrainingTypeDAO, использующая JDBC для взаимодействия с базой данных.
 */
@RequiredArgsConstructor
public class JdbcTrainingTypeDAOImpl implements TrainingTypeDAO {
    private final Connection connection;

    @Override
    public Optional<TrainingType> findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_TRAINING_TYPE_BY_ID)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            TrainingType type = null;
            if (resultSet.next()) {
                type = trainingTypeBuilder(resultSet);
            }
            return Optional.ofNullable(type);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<TrainingType> findAll() {
        List<TrainingType> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TRAINING_TYPE)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(trainingTypeBuilder(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TRAINING_TYPE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, trainingType.getType());
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            if (key.next()) {
                trainingType.setId(key.getLong("id"));
            }
            return trainingType;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<TrainingType> findByType(String type) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_TRAINING_TYPE_BY_TYPE)) {
            preparedStatement.setString(1, type);

            ResultSet resultSet = preparedStatement.executeQuery();
            TrainingType trainingType = null;
            if (resultSet.next()) {
                trainingType = trainingTypeBuilder(resultSet);
            }
            return Optional.ofNullable(trainingType);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private TrainingType trainingTypeBuilder(ResultSet resultSet) throws SQLException {
        return TrainingType.builder()
                .id(resultSet.getLong("id"))
                .type(resultSet.getString("type"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .build();
    }
}