package io.kragelv.library.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.dao.GenericDAO;

public abstract class GenericDAOImpl<T> extends AbstractDAOImpl implements GenericDAO<T> {

    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected Optional<T> mapResultSetToOptional(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(mapResultSetToEntity(resultSet));
        } else {
            return Optional.empty();
        }
    }

    protected LocalDateTime getLocalDateTimeFromNullableTimestamp(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    protected UUID getUUIDForStatement(UUID id) {
        return id != null ? id : UUID.randomUUID();
    }
}
