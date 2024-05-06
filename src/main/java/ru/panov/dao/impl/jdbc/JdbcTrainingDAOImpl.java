package ru.panov.dao.impl.jdbc;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.panov.dao.TrainingDAO;
import ru.panov.model.Training;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.panov.util.SQLUtil.*;

/**
 * Реализация интерфейса TrainingDAO, использующая JDBC для взаимодействия с базой данных.
 */
@Repository
@RequiredArgsConstructor
public class JdbcTrainingDAOImpl implements TrainingDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Training> findById(Long id, Long userId) {
        return jdbcTemplate.query(FIND_TRAINING_BY_ID_AND_USER_ID, rowMapper(), id, userId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Training> findAll() {
        return Collections.unmodifiableList(jdbcTemplate.query(FIND_ALL_TRAINING, rowMapper()));
    }

    @Override
    public Training save(Training entity, Long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_TRAINING, new String[]{"id"});
            ps.setLong(1, entity.getTypeId());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getCreated()));
            ps.setTimestamp(3, Timestamp.valueOf(entity.getUpdated()));
            ps.setDouble(4, entity.getCountCalories());
            ps.setTime(5, Time.valueOf(entity.getTrainingTime()));
            ps.setString(6, entity.getAdditionalInfo());
            ps.setLong(7, userId);
            return ps;
        }, keyHolder);
        entity.setId((Long) keyHolder.getKey());
        return entity;
    }

    @Override
    public Training update(Training entity, Long userId) {
        jdbcTemplate.update(UPDATE_TRAINING,
                entity.getTypeId(),
                Timestamp.valueOf(entity.getCreated()),
                Timestamp.valueOf(entity.getUpdated()),
                entity.getCountCalories(),
                Time.valueOf(entity.getTrainingTime()),
                entity.getAdditionalInfo(),
                entity.getId(),
                userId);
        return entity;

    }

    @Override
    public boolean delete(Long id, Long userId) {
        return jdbcTemplate.update(DELETE_TRAINING, id, userId) > 0;
    }

    @Override
    public Double caloriesSpentOverPeriod(LocalDateTime start, LocalDateTime end, Long userId) {
        return jdbcTemplate.queryForObject(SUM_CALORIES_SPENT_OVER_PERIOD, Double.class, userId, Timestamp.valueOf(start), Timestamp.valueOf(end));
    }

    @Override
    public List<Training> findAllByUserId(Long userId) {
        return Collections.unmodifiableList(jdbcTemplate.query(FIND_ALL_TRAINING_BY_USER_ID, rowMapper(), userId));
    }

    @Override
    public List<Training> findAll(int limit, int offset) {
        return Collections.unmodifiableList(jdbcTemplate.query(FIND_ALL_TRAINING_LIMIT_OFFSET, rowMapper(), limit, offset));
    }

    private RowMapper<Training> rowMapper() {
        return (ResultSet resultSet, int rowNum) -> Training.builder()
                .id(resultSet.getLong("id"))
                .typeId(resultSet.getLong("type_id"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                .countCalories(resultSet.getDouble("count_calories"))
                .trainingTime(resultSet.getTime("training_time").toLocalTime())
                .additionalInfo(resultSet.getString("additional_info"))
                .userId(resultSet.getLong("user_id"))
                .build();
    }
}