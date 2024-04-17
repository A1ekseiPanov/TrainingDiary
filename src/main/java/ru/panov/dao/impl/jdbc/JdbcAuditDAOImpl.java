package ru.panov.dao.impl.jdbc;


import ru.panov.dao.AuditDAO;
import ru.panov.exception.DaoException;
import ru.panov.model.Audit;
import ru.panov.model.AuditType;
import ru.panov.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcAuditDAOImpl implements AuditDAO {
    public static final String FIND_ALL_AUDIT =
            "SELECT id, created, class_name, method_name, audit_type, username FROM dbo.audits;";
    public static final String CREATE_AUDIT =
            "INSERT INTO dbo.audits (class_name, method_name, audit_type, username) VALUES (?,?,?,?)";
    public static final String FIND_AUDIT_BY_ID =
            "SELECT id, created, class_name, method_name, audit_type, username FROM dbo.audits WHERE id = ?;";

    @Override
    public Optional<Audit> findById(Long id) {
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_AUDIT_BY_ID)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Audit audit = null;
            if (resultSet.next()) {
                audit = auditBuilder(resultSet);
            }
            return Optional.ofNullable(audit);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Audit> findAll() {
        List<Audit> audits = new ArrayList<>();
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_AUDIT)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                audits.add(auditBuilder(resultSet));
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Collections.unmodifiableList(audits);
    }

    @Override
    public Audit save(Audit audit) {
        try (Connection connection = ConnectionUtil.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_AUDIT)) {
            preparedStatement.setString(1, audit.getClassName());
            preparedStatement.setString(2, audit.getMethodName());
            preparedStatement.setString(3, audit.getType().toString());
            preparedStatement.setString(4, audit.getUsername());
            preparedStatement.executeUpdate();

            ResultSet key = preparedStatement.getGeneratedKeys();
            if (key.next()) {
                audit.setId(key.getLong("id"));
            }
            return audit;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Audit auditBuilder(ResultSet resultSet) throws SQLException {
        return Audit.builder()
                .id(resultSet.getLong("id"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .className(resultSet.getString("class_name"))
                .methodName(resultSet.getString("method_name"))
                .type(AuditType.valueOf(resultSet.getString("audit_type")))
                .username(resultSet.getString("username"))
                .build();
    }
}