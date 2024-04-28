package ru.panov.dao.impl.jdbc;


import lombok.RequiredArgsConstructor;
import ru.panov.dao.UserDAO;
import ru.panov.exception.DaoException;
import ru.panov.model.Role;
import ru.panov.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.panov.util.SQLUtil.*;

/**
 * Реализация интерфейса UserDAO, использующая JDBC для взаимодействия с базой данных.
 */
@RequiredArgsConstructor
public class JdbcUserDAOImpl implements UserDAO {
    private final Connection connection;

    @Override
    public Optional<User> findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;

            if (resultSet.next()) {
                user = userBuilder(resultSet);
            }
            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(userBuilder(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public User save(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(user.getCreated()));
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            if (key.next()) {
                user.setId(key.getLong("id"));
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = userBuilder(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private User userBuilder(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .username(resultSet.getString("username"))
                .role(Role.valueOf(resultSet.getString("role")))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .password(resultSet.getString("password"))
                .build();
    }
}