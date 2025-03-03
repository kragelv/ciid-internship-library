package io.kragelv.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.kragelv.library.dao.BookGenreDAO;
import io.kragelv.library.dao.exception.DAOException;

public class BookGenreDAOImpl extends AbstractDAOImpl implements BookGenreDAO {

    private static final String INSERT_SQL = "INSERT INTO book_genres (book_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_BY_BOOK_SQL = "DELETE FROM book_genres WHERE book_id = ?";
    private static final String DELETE_BY_GENRE_SQL = "DELETE FROM book_genres WHERE genre_id = ?";
    private static final String SELECT_GENRES_BY_BOOK = "SELECT genre_id FROM book_genres WHERE book_id = ?";
    private static final String SELECT_BOOKS_BY_GENRE = "SELECT book_id FROM book_genres WHERE genre_id = ?";

    @Override
    public void create(UUID bookId, UUID genreId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setObject(1, bookId);
            statement.setObject(2, genreId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error inserting book-genre relation", e);
        }
    }

    @Override
    public void deleteByBookId(UUID bookId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_BOOK_SQL)) {

            statement.setObject(1, bookId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error deleting book-genre relations by book ID", e);
        }
    }

    @Override
    public void deleteByGenreId(UUID genreId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_GENRE_SQL)) {

            statement.setObject(1, genreId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error deleting book-genre relations by genre ID", e);
        }
    }

    @Override
    public List<UUID> getGenresByBook(UUID bookId) {
        List<UUID> genreIds = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_GENRES_BY_BOOK)) {

            statement.setObject(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    genreIds.add(resultSet.getObject("genre_id", UUID.class));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving genres for book ID " + bookId, e);
        }
        return genreIds;
    }

    @Override
    public List<UUID> getBooksByGenre(UUID genreId) {
        List<UUID> bookIds = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BOOKS_BY_GENRE)) {

            statement.setObject(1, genreId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookIds.add(resultSet.getObject("book_id", UUID.class));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving books for genre ID " + genreId, e);
        }
        return bookIds;
    }
}
