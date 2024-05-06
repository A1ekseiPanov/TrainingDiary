package ru.panov.dao.impl.jdbc;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.panov.dao.UserDAO;
import ru.panov.model.Role;
import ru.panov.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.panov.util.SQLUtil.*;

/**
 * Реализация интерфейса UserDAO, использующая JDBC для взаимодействия с базой данных.
 */
@Repository
@RequiredArgsConstructor
public class JdbcUserDAOImpl implements UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query(FIND_USER_BY_ID, rowMapper(), id).stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return Collections.unmodifiableList(jdbcTemplate.query(FIND_ALL_USERS, rowMapper()));
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_USER, new String[]{"id"});
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().toString());
            ps.setTimestamp(4, Timestamp.valueOf(user.getCreated()));
            return ps;

        }, keyHolder);
        user.setId((Long) keyHolder.getKey());
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query(FIND_USER_BY_USERNAME, rowMapper(), username).stream().findFirst();
    }

    private RowMapper<User> rowMapper() {
        return (ResultSet resultSet, int rowNum) -> User.builder()
                .id(resultSet.getLong("id"))
                .username(resultSet.getString("username"))
                .role(Role.valueOf(resultSet.getString("role")))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .password(resultSet.getString("password"))
                .build();
    }
}