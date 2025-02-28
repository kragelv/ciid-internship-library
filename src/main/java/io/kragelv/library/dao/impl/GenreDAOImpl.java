package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.GenreDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Genre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GenreDAOImpl extends GenericDAOImpl<Genre> implements GenreDAO {

    @Override
    protected Genre mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = (UUID) resultSet.getObject("id");
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }

    @Override
    public void add(Genre genre) throws DAOException {
        String sql = "INSERT INTO genres (name) VALUES (?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error adding genre", e);
        }
    }

    @Override
    public void update(Genre genre) throws DAOException {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre.getName());
            statement.setObject(2, genre.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating genre", e);
        }
    }

    @Override
    public Optional<Genre> getById(UUID id) throws DAOException {
        String sql = "SELECT * FROM genres WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Error fetching genre by ID", e);
        }
    }

    @Override
    public List<Genre> getAll() throws DAOException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genres";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                genres.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching all genres", e);
        }
        return genres;
    }

    @Override
    public void delete(UUID id) throws DAOException {
        String sql = "DELETE FROM genres WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting genre", e);
        }
    }
}
