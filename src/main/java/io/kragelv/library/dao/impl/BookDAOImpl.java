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

    private static final String INSERT_SQL = "INSERT INTO books (id, title, author_id, published_year, available_copies) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE books SET title = ?, author_id = ?, published_year = ?, available_copies = ? WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT id, title, author_id, published_year, available_copies FROM books WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, title, author_id, published_year, available_copies FROM books";
    private static final String DELETE_SQL = "DELETE FROM books WHERE id = ?";
    private static final String SELECT_BY_AUTHOR_SQL = "SELECT id, title, author_id, published_year, available_copies FROM books WHERE author_id = ?";

    @Override
    protected Book mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String title = resultSet.getString("title");
        UUID authorId = resultSet.getObject("author_id", UUID.class);
        
        // Handle nullable INT field
        Integer publishedYear = resultSet.getInt("published_year");
        if (resultSet.wasNull()) {
            publishedYear = null;
        }

        Integer availableCopies = resultSet.getInt("available_copies");
        return new Book(id, title, authorId, publishedYear, availableCopies);
    }

    @Override
    public Book create(Book book) {
        UUID bookId = getUUIDForStatement(book.getId());

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setObject(1, bookId);
            statement.setString(2, book.getTitle());
            statement.setObject(3, book.getAuthorId());

            if (book.getPublishedYear() != null) {
                statement.setInt(4, book.getPublishedYear());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            statement.setInt(5, book.getAvailableCopies());
            statement.executeUpdate();
            
            return new Book(bookId, book.getTitle(), book.getAuthorId(), book.getPublishedYear(), book.getAvailableCopies());

        } catch (SQLException e) {
            throw new DAOException("Error creating book", e);
        }
    }

    @Override
    public void update(Book book) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, book.getTitle());
            statement.setObject(2, book.getAuthorId());

            if (book.getPublishedYear() != null) {
                statement.setInt(3, book.getPublishedYear());
            } else {
                statement.setNull(3, Types.INTEGER);
            }

            statement.setInt(4, book.getAvailableCopies());
            statement.setObject(5, book.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error updating book", e);
        }
    }

    @Override
    public Optional<Book> getById(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);

        } catch (SQLException e) {
            throw new DAOException("Error fetching book by ID", e);
        }
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
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
    public void delete(UUID id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setObject(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error deleting book", e);
        }
    }

    @Override
    public List<Book> getBooksByAuthor(UUID authorId) {
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_AUTHOR_SQL)) {

            statement.setObject(1, authorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(mapResultSetToEntity(resultSet));
            }
            return books;

        } catch (SQLException e) {
            throw new DAOException("Error fetching books by author", e);
        }
    }
}