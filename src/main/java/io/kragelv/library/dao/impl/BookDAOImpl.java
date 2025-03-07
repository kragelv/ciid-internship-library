package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.BookDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Author;
import io.kragelv.library.model.Book;
import io.kragelv.library.model.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BookDAOImpl extends GenericDAOImpl<Book> implements BookDAO {

    private static final String INSERT_SQL = "INSERT INTO books (id, title, author_id, published_year, available_copies) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE books SET title = ?, author_id = ?, published_year = ?, available_copies = ? WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT id, title, author_id, published_year, available_copies FROM books WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, title, author_id, published_year, available_copies FROM books";
    private static final String DELETE_SQL = "DELETE FROM books WHERE id = ?";
    private static final String SELECT_BY_AUTHOR_SQL = "SELECT id, title, author_id, published_year, available_copies FROM books WHERE author_id = ?";
    private static final String SELECT_ALL_JOIN_AUTHOR_GENRES_SQL = "SELECT b.id AS book_id, b.title AS book_title, " +
            "b.published_year AS book_published_year, b.available_copies AS book_available_copies, " +
            "g.id AS genre_id, g.name AS genre_name, a.id AS author_id, a.first_name AS author_first_name, " +
            "a.middle_name AS author_middle_name, a.last_name AS author_last_name, a.birth_year AS author_birth_year " +
            "FROM books b " +
            "LEFT JOIN book_genres bg ON b.id = bg.book_id " +
            "LEFT JOIN genres g ON bg.genre_id = g.id " +
            "LEFT JOIN authors a ON b.author_id = a.id";
    private static final String SELECT_BY_ID_JOIN_AUTHOR_GENRES_SQL = "SELECT b.id AS book_id, b.title AS book_title, " +
            "b.published_year AS book_published_year, b.available_copies AS book_available_copies, " +
            "g.id AS genre_id, g.name AS genre_name, a.id AS author_id, a.first_name AS author_first_name, " +
            "a.middle_name AS author_middle_name, a.last_name AS author_last_name, a.birth_year AS author_birth_year " +
            "FROM books b " +
            "LEFT JOIN book_genres bg ON b.id = bg.book_id " +
            "LEFT JOIN genres g ON bg.genre_id = g.id " +
            "LEFT JOIN authors a ON b.author_id = a.id " +
            "WHERE b.id = ?";

    @Override
    protected Book mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String title = resultSet.getString("title");
        UUID authorId = resultSet.getObject("author_id", UUID.class);
        Integer publishedYear = resultSet.getInt("published_year");
        if (resultSet.wasNull()) {
            publishedYear = null;
        }
        Integer availableCopies = resultSet.getInt("available_copies");
        return new Book(id, title, authorId, publishedYear, availableCopies);
    }

    protected List<Book> mapResultSetToJoinedEntities(ResultSet resultSet) throws SQLException {
        Map<UUID, Book> bookMap = new HashMap<>();
        while (resultSet.next()) {
            UUID bookId = resultSet.getObject("book_id", UUID.class);
            Book book;
            if (bookMap.containsKey(bookId)) {
                book = bookMap.get(bookId);
            } else {
                book = new Book();

                String bookTitle = resultSet.getString("book_title");
                Integer bookPublishedYear = resultSet.getInt("book_published_year");
                if (resultSet.wasNull()) {
                    bookPublishedYear = null;
                }
                Integer bookAvailableCopies = resultSet.getInt("book_available_copies");
  
                book.setId(bookId);
                book.setTitle(bookTitle);
                book.setPublishedYear(bookPublishedYear);
                book.setAvailableCopies(bookAvailableCopies);

                UUID authorId = resultSet.getObject("author_id", UUID.class);
                if (authorId != null) {
                    Author author = new Author();
                    author.setId(authorId);
                    author.setFirstName(resultSet.getString("author_first_name"));
                    author.setMiddleName(resultSet.getString("author_middle_name"));
                    author.setLastName(resultSet.getString("author_last_name"));
                    Integer birthYear = resultSet.getInt("author_birth_year");
                    if (resultSet.wasNull()) {
                        birthYear = null;
                    }
                    author.setBirthYear(birthYear);
                    book.setAuthor(author);
                    book.setAuthorId(authorId);
                }

                book.setGenres(new ArrayList<>());
                bookMap.put(bookId, book);
            }

            UUID genreId = resultSet.getObject("genre_id", UUID.class);
            String genreName = resultSet.getString("genre_name");

            if (genreId != null) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(genreName);
                book.getGenres().add(genre);
            }
        }
        return new ArrayList<>(bookMap.values());
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

            return new Book(bookId, book.getTitle(), book.getAuthorId(), book.getPublishedYear(),
                    book.getAvailableCopies());

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
    public Optional<Book> getByIdIncludeAuthorAndGenres(UUID id) {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_JOIN_AUTHOR_GENRES_SQL)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return Optional.ofNullable(getFirstOrNull(mapResultSetToJoinedEntities(resultSet)));
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

    public List<Book> getAllBooksIncludeAuthorAndGenres() {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_JOIN_AUTHOR_GENRES_SQL);
                ResultSet resultSet = statement.executeQuery()) {
                return mapResultSetToJoinedEntities(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Error fetching all books", e);
        }
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