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

    @Override
    protected Author mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = (UUID) resultSet.getObject("id");
        String firstName = resultSet.getString("first_name");
        String middleName = resultSet.getString("middle_name");
        String lastName = resultSet.getString("last_name");
        int birthYear = resultSet.getInt("birth_year");
        return new Author(id, firstName, middleName, lastName, birthYear);
    }

    @Override
    public void add(Author author) throws DAOException {
        String sql = "INSERT INTO authors (first_name, middle_name, last_name, birth_year) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getMiddleName());
            statement.setString(3, author.getLastName());
            statement.setInt(4, author.getBirthYear());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error adding author", e);
        }
    }

    @Override
    public void update(Author author) throws DAOException {
        String sql = "UPDATE authors SET first_name = ?, middle_name = ?, last_name = ?, birth_year = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getMiddleName());
            statement.setString(3, author.getLastName());
            statement.setInt(4, author.getBirthYear());
            statement.setObject(5, author.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating author", e);
        }
    }

    @Override
    public Optional<Author> getById(UUID id) throws DAOException {
        String sql = "SELECT id, first_name, middle_name, last_name, birth_year FROM authors WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Error fetching author by ID", e);
        }
    }

    @Override
    public List<Author> getAll() throws DAOException {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT id, first_name, middle_name, last_name, birth_year FROM authors";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
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
    public void delete(UUID id) throws DAOException {
        String sql = "DELETE FROM authors WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting author", e);
        }
    }
}
