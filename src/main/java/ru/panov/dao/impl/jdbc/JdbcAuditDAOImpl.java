package ru.panov.dao.impl.jdbc;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.panov.dao.AuditDAO;
import ru.panov.model.Audit;
import ru.panov.model.AuditType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static ru.panov.util.SQLConstants.*;

/**
 * Реализация интерфейса AuditDAO, использующая Spring JDBC Template для взаимодействия с базой данных.
 */
@Repository
@RequiredArgsConstructor
public class JdbcAuditDAOImpl implements AuditDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Audit> findById(Long id) {
        return jdbcTemplate.query(FIND_AUDIT_BY_ID, rowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Audit> findAll() {
        return jdbcTemplate.query(FIND_ALL_AUDIT, rowMapper());
    }

    @Override
    public Audit save(Audit audit) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_AUDIT, new String[]{"id"});
            ps.setString(1, audit.getClassName());
            ps.setString(2, audit.getMethodName());
            ps.setString(3, audit.getType().toString());
            ps.setString(4, audit.getUsername());
            return ps;
        }, keyHolder);
        audit.setId((Long) keyHolder.getKey());
        return audit;
    }

    private RowMapper<Audit> rowMapper() {
        return (ResultSet resultSet, int rowNum) -> Audit.builder()
                .id(resultSet.getLong("id"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .className(resultSet.getString("class_name"))
                .methodName(resultSet.getString("method_name"))
                .type(AuditType.valueOf(resultSet.getString("audit_type")))
                .username(resultSet.getString("username"))
                .build();
    }
}