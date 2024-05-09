package ru.panov.dao.impl.jdbc;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.panov.dao.TrainingTypeDAO;
import ru.panov.model.TrainingType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.panov.util.SQLConstants.*;

/**
 * Реализация интерфейса TrainingTypeDAO, использующая Spring JDBC Template для взаимодействия с базой данных.
 */
@Repository
@RequiredArgsConstructor
public class JdbcTrainingTypeDAOImpl implements TrainingTypeDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<TrainingType> findById(Long id) {
        return jdbcTemplate.query(FIND_TRAINING_TYPE_BY_ID, rowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<TrainingType> findAll() {
        return Collections.unmodifiableList(jdbcTemplate.query(FIND_ALL_TRAINING_TYPE, rowMapper()));
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_TRAINING_TYPE, new String[]{"id"});
            ps.setString(1, trainingType.getType());
            return ps;
        }, keyHolder);
        trainingType.setId((Long) keyHolder.getKey());
        return trainingType;
    }

    @Override
    public Optional<TrainingType> findByType(String type) {
        return jdbcTemplate.query(FIND_TRAINING_TYPE_BY_TYPE, rowMapper(), type)
                .stream()
                .findFirst();
    }

    private RowMapper<TrainingType> rowMapper() {
        return (ResultSet resultSet, int rowNum) -> TrainingType.builder()
                .id(resultSet.getLong("id"))
                .type(resultSet.getString("type"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .build();
    }
}