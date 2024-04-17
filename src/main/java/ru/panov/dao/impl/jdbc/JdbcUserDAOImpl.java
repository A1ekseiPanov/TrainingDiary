package ru.panov.dao.impl.jdbc;


import ru.panov.dao.UserDAO;
import ru.panov.exception.DaoException;
import ru.panov.model.Role;
import ru.panov.model.User;
import ru.panov.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcUserDAOImpl implements UserDAO {
    public static final String FIND_USER_BY_ID =
            "SELECT id, username, password, role, created FROM dbo.users WHERE id = ?";
    public static final String FIND_ALL_USER =
            "SELECT id, username, password, role, created FROM dbo.users;";
    public static final String FIND_USER_BY_USERNAME =
            "SELECT id, username, password, role, created FROM dbo.users WHERE username = ?";
    public static final String SAVE_USER = "INSERT INTO dbo.users (username, password, role, created) VALUES (?,?,?,?)";

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {
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
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USER)) {
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
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(user.getCreated()));
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            while (key.next()) {
                user.setId(key.getLong("id"));
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)) {
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