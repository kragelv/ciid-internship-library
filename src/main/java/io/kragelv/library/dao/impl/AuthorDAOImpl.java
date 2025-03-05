package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.AuthorDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public class AuthorDAOImpl extends GenericDAOImpl<Author> implements AuthorDAO {

    private static final String INSERT_SQL = "INSERT INTO authors (id, first_name, middle_name, last_name, birth_year) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE authors SET first_name = ?, middle_name = ?, last_name = ?, birth_year = ? WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT id, first_name, middle_name, last_name, birth_year FROM authors WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, first_name, middle_name, last_name, birth_year FROM authors";
    private static final String DELETE_SQL = "DELETE FROM authors WHERE id = ?";

    @Override
    protected Author mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String firstName = resultSet.getString("first_name");
        String middleName = resultSet.getString("middle_name");
        String lastName = resultSet.getString("last_name");
        Integer birthYear = resultSet.getInt("birth_year");
        if (resultSet.wasNull()) {
            birthYear = null;
        }

        return new Author(id, firstName, middleName, lastName, birthYear);
    }

    @Override
    public Author create(Author author) {
        UUID authorId = getUUIDForStatement(author.getId());

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setObject(1, authorId);
            statement.setString(2, author.getFirstName());
            statement.setString(3, author.getMiddleName());
            statement.setString(4, author.getLastName());

            if (author.getBirthYear() != null) {
                statement.setInt(5, author.getBirthYear());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.executeUpdate();
            return new Author(authorId, author.getFirstName(), author.getMiddleName(), author.getLastName(), author.getBirthYear());

        } catch (SQLException e) {
            throw new DAOException("Error creating author", e);
        }
    }

    @Override
    public void update(Author author) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getMiddleName());
            statement.setString(3, author.getLastName());

            if (author.getBirthYear() != null) {
                statement.setInt(4, author.getBirthYear());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            statement.setObject(5, author.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error updating author", e);
        }
    }

    @Override
    public Optional<Author> getById(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);

        } catch (SQLException e) {
            throw new DAOException("Error fetching author by ID", e);
        }
    }

    @Override
    public List<Author> getAll() {
        List<Author> authors = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                authors.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching all authors", e);
        }
        return authors;
    }

    @Override
    public void delete(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setObject(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error deleting author", e);
        }
    }
}