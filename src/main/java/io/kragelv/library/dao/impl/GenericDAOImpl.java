package io.kragelv.library.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import io.kragelv.library.dao.GenericDAO;
import io.kragelv.library.dao.connection.ConnectionManager;

public abstract class GenericDAOImpl<T> implements GenericDAO<T> {

    protected Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    protected void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            try {
                if (resource != null) {
                    resource.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
}
