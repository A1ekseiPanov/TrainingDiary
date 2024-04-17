package ru.panov.dao.impl.jdbc;


import ru.panov.dao.TrainingTypeDAO;
import ru.panov.exception.DaoException;
import ru.panov.model.TrainingType;
import ru.panov.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcTrainingTypeDAOImpl implements TrainingTypeDAO {
    public static final String FIND_ALL_TRAINING_TYPE =
            "SELECT id, type, created FROM dbo.training_types;";
    public static final String CREATE_TRAINING_TYPE =
            "INSERT INTO dbo.training_types (type) VALUES (?)";
    public static final String FIND_TRAINING_TYPE_BY_ID =
            "SELECT id, type, created FROM dbo.training_types WHERE id = ?;";

    @Override
    public Optional<TrainingType> findById(Long id) {
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_TRAINING_TYPE_BY_ID)) {
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
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TRAINING_TYPE)) {

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
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TRAINING_TYPE)) {
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

    private TrainingType trainingTypeBuilder(ResultSet resultSet) throws SQLException {
        return TrainingType.builder()
                .id(resultSet.getLong("id"))
                .type(resultSet.getString("type"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .build();
    }
}