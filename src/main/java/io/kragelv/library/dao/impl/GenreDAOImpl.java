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

    private static final String INSERT_SQL = "INSERT INTO genres (id, name) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE genres SET name = ? WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT id, name FROM genres WHERE id = ?";
    private static final String SELECT_BY_NAME_SQL = "SELECT id, name FROM genres WHERE name = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, name FROM genres";
    private static final String DELETE_SQL = "DELETE FROM genres WHERE id = ?";

    @Override
    protected Genre mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }

    @Override
    public Genre create(Genre genre) {
        UUID genreId = getUUIDForStatement(genre.getId());

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setObject(1, genreId);
            statement.setString(2, genre.getName());
            statement.executeUpdate();

            return new Genre(genreId, genre.getName());

        } catch (SQLException e) {
            throw new DAOException("Error creating genre", e);
        }
    }

    @Override
    public void update(Genre genre) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, genre.getName());
            statement.setObject(2, genre.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error updating genre", e);
        }
    }

    @Override
    public Optional<Genre> getById(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);

        } catch (SQLException e) {
            throw new DAOException("Error fetching genre by ID", e);
        }
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> genres = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
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
    public void delete(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setObject(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error deleting genre", e);
        }
    }

    @Override
    public Optional<Genre> getByName(String name) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_SQL)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);

        } catch (SQLException e) {
            throw new DAOException("Error fetching genre by name", e);
        }
    }
}
