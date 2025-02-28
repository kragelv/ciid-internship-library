package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.ReaderDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReaderDAOImpl extends GenericDAOImpl<Reader> implements ReaderDAO {

    @Override
    protected Reader mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = (UUID) resultSet.getObject("id");
        String fullname = resultSet.getString("fullname");
        String email = resultSet.getString("email");
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        return new Reader(id,
                fullname,
                email,
                getLocalDateTimeFromNullableTimestamp(createdAt));
    }

    @Override
    public void add(Reader reader) throws DAOException {
        String sql = "INSERT INTO readers (fullname, email) VALUES (?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reader.getFullname());
            statement.setString(2, reader.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error adding reader", e);
        }
    }

    @Override
    public void update(Reader reader) throws DAOException {
        String sql = "UPDATE readers SET fullname = ?, email = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reader.getFullname());
            statement.setString(2, reader.getEmail());
            statement.setObject(3, reader.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating reader", e);
        }
    }

    @Override
    public Optional<Reader> getById(UUID id) throws DAOException {
        String sql = "SELECT id, fullname, email, created_at FROM readers WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Error fetching reader by ID", e);
        }
    }

    @Override
    public List<Reader> getAll() throws DAOException {
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT id, fullname, email, created_at` FROM readers";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
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
    public void delete(UUID id) throws DAOException {
        String sql = "DELETE FROM readers WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting reader", e);
        }
    }
}
