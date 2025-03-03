package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.ReaderDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Reader;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReaderDAOImpl extends GenericDAOImpl<Reader> implements ReaderDAO {

    private static final String INSERT_SQL = "INSERT INTO readers (id, fullname, email) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE readers SET fullname = ?, email = ? WHERE id = ?";
    private static final String SELECT_BY_EMAIL_SQL = "SELECT id, fullname, email, created_at FROM readers WHERE email = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT id, fullname, email, created_at FROM readers WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, fullname, email, created_at FROM readers";
    private static final String DELETE_SQL = "DELETE FROM readers WHERE id = ?";

    @Override
    protected Reader mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String fullname = resultSet.getString("fullname");
        String email = resultSet.getString("email");

        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
        LocalDateTime createdAt = getLocalDateTimeFromNullableTimestamp(createdAtTimestamp);

        return new Reader(id, fullname, email, createdAt);
    }

    @Override
    public Reader create(Reader reader) {
        UUID readerId = getUUIDForStatement(reader.getId());

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setObject(1, readerId);
            statement.setString(2, reader.getFullname());
            statement.setString(3, reader.getEmail());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Timestamp createdAtTimestamp = generatedKeys.getTimestamp("created_at");
                    LocalDateTime createdAt = createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null;
                    return new Reader(readerId, reader.getFullname(), reader.getEmail(), createdAt);
                }
            }

            return new Reader(readerId, reader.getFullname(), reader.getEmail(), null);

        } catch (SQLException e) {
            throw new DAOException("Error creating reader", e);
        }
    }

    @Override
    public void update(Reader reader) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, reader.getFullname());
            statement.setString(2, reader.getEmail());
            statement.setObject(3, reader.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error updating reader", e);
        }
    }

    @Override
    public Optional<Reader> getById(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);

        } catch (SQLException e) {
            throw new DAOException("Error fetching reader by ID", e);
        }
    }

    @Override
    public List<Reader> getAll() {
        List<Reader> readers = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                readers.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching all readers", e);
        }
        return readers;
    }

    @Override
    public void delete(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setObject(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error deleting reader", e);
        }
    }

    @Override
    public Optional<Reader> getByEmail(String email) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL_SQL)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);

        } catch (SQLException e) {
            throw new DAOException("Error fetching reader by email", e);
        }
    }

    
}
