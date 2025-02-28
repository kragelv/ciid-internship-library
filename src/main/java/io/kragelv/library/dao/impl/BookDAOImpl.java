package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.BookDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookDAOImpl extends GenericDAOImpl<Book> implements BookDAO {

    @Override
    protected Book mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = (UUID) resultSet.getObject("id");
        String title = resultSet.getString("title");
        UUID authorId = (UUID) resultSet.getObject("author_id");
        int publishedYear = resultSet.getInt("published_year");
        int availableCopies = resultSet.getInt("available_copies");
        return new Book(id, title, authorId, publishedYear, availableCopies);
    }

    @Override
    public void add(Book book) throws DAOException {
        String sql = "INSERT INTO books (id, title, author_id, published_year, available_copies) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setObject(3, book.getAuthorId());
            statement.setInt(4, book.getPublishedYear());
            statement.setInt(5, book.getAvailableCopies());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error adding book", e);
        }
    }

    @Override
    public void update(Book book) throws DAOException {
        String sql = "UPDATE books SET title = ?, author_id = ?, published_year = ?, available_copies = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setObject(2, book.getAuthorId());
            statement.setInt(3, book.getPublishedYear());
            statement.setInt(4, book.getAvailableCopies());
            statement.setObject(5, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating book", e);
        }
    }

    @Override
    public Optional<Book> getById(UUID id) throws DAOException {
        String sql = "SELECT id, title, author_id, published_year, available_copies FROM books WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Error fetching book by ID", e);
        }
    }

    @Override
    public List<Book> getAll() throws DAOException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author_id, published_year, available_copies FROM books";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                books.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching all books", e);
        }
        return books;
    }

    @Override
    public void delete(UUID id) throws DAOException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting book", e);
        }
    }
}
