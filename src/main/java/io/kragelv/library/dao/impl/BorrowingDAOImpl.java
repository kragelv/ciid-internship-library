package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.BorrowingDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Borrowing;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BorrowingDAOImpl extends GenericDAOImpl<Borrowing> implements BorrowingDAO {

    private static final String INSERT_SQL = "INSERT INTO borrowings (id, reader_id, book_id, borrowed_at, due_date) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE borrowings SET reader_id = ?, book_id = ?, borrowed_at = ?, due_date = ?, returned_at = ? WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT id, reader_id, book_id, borrowed_at, due_date, returned_at FROM borrowings WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, reader_id, book_id, borrowed_at, due_date, returned_at FROM borrowings";
    private static final String DELETE_SQL = "DELETE FROM borrowings WHERE id = ?";
    private static final String GET_BY_BOOK_ID_SQL = "SELECT id, reader_id, book_id, borrowed_at, due_date, returned_at FROM borrowings WHERE book_id = ?";
    private static final String GET_BY_READER_ID_SQL = "SELECT id, reader_id, book_id, borrowed_at, due_date, returned_at FROM borrowings WHERE reader_id = ?";

    @Override
    protected Borrowing mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        UUID readerId = resultSet.getObject("reader_id", UUID.class);
        UUID bookId = resultSet.getObject("book_id", UUID.class);

        LocalDateTime borrowedAt = resultSet.getTimestamp("borrowed_at").toLocalDateTime();
        LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();
        LocalDateTime returnedAt = getLocalDateTimeFromNullableTimestamp(resultSet.getTimestamp("returned_at"));

        return new Borrowing(id, readerId, bookId, borrowedAt, dueDate, returnedAt);
    }

    @Override
    public Borrowing create(Borrowing borrowing) {
        UUID borrowingId = getUUIDForStatement(borrowing.getId());

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setObject(1, getUUIDForStatement(borrowing.getId()));
            statement.setObject(2, borrowing.getReaderId());
            statement.setObject(3, borrowing.getBookId());
            statement.setTimestamp(4, Timestamp.valueOf(borrowing.getBorrowedAt()));
            statement.setDate(5, Date.valueOf(borrowing.getDueDate()));

            statement.executeUpdate();

            return new Borrowing(borrowingId, borrowing.getReaderId(), borrowing.getBookId(), borrowing.getBorrowedAt(),
                    borrowing.getDueDate(), borrowing.getReturnedAt());

        } catch (SQLException e) {
            throw new DAOException("Error creating borrowing", e);
        }
    }

    @Override
    public void update(Borrowing borrowing) {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setObject(1, borrowing.getReaderId());
            statement.setObject(2, borrowing.getBookId());
            statement.setTimestamp(3, Timestamp.valueOf(borrowing.getBorrowedAt()));
            statement.setDate(4, Date.valueOf(borrowing.getDueDate()));
            statement.setTimestamp(5,
                    borrowing.getReturnedAt() != null ? Timestamp.valueOf(borrowing.getReturnedAt()) : null);
            statement.setObject(6, borrowing.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error updating borrowing", e);
        }
    }

    @Override
    public Optional<Borrowing> getById(UUID id) {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            return mapResultSetToOptional(resultSet);

        } catch (SQLException e) {
            throw new DAOException("Error fetching borrowing by ID", e);
        }
    }

    @Override
    public List<Borrowing> getAll() {
        List<Borrowing> borrowings = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                borrowings.add(mapResultSetToEntity(resultSet));
            }

        } catch (SQLException e) {
            throw new DAOException("Error fetching all borrowings", e);
        }
        return borrowings;
    }

    @Override
    public void delete(UUID id) {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setObject(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error deleting borrowing", e);
        }
    }

    @Override
    public List<Borrowing> getByBookId(UUID bookId) {
        List<Borrowing> borrowings = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_BY_BOOK_ID_SQL)) {

            statement.setObject(1, bookId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Borrowing borrowing = mapResultSetToEntity(resultSet);
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching borrowings by book ID", e);
        }

        return borrowings;
    }

    @Override
    public List<Borrowing> getByReaderId(UUID readerId) {
        List<Borrowing> borrowings = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_BY_READER_ID_SQL)) {

            statement.setObject(1, readerId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Borrowing borrowing = mapResultSetToEntity(resultSet);
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching borrowings by reader ID", e);
        }

        return borrowings;
    }
}