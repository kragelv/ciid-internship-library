package io.kragelv.library.dao.impl;

import io.kragelv.library.dao.BorrowingDAO;
import io.kragelv.library.dao.exception.DAOException;
import io.kragelv.library.model.Borrowing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BorrowingDAOImpl extends GenericDAOImpl<Borrowing> implements BorrowingDAO {

    @Override
    protected Borrowing mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        UUID id = (UUID) resultSet.getObject("id");
        UUID readerId = (UUID) resultSet.getObject("reader_id");
        UUID bookId = (UUID) resultSet.getObject("book_id");
        Timestamp borrowedAt = resultSet.getTimestamp("borrowed_at");
        Date dueDate = resultSet.getDate("due_date");
        Timestamp returnedAt = resultSet.getTimestamp("returned_at");
        return new Borrowing(id,
                readerId,
                bookId,
                borrowedAt.toLocalDateTime(),
                dueDate.toLocalDate(),
                getLocalDateTimeFromNullableTimestamp(returnedAt));
    }

    @Override
    public void add(Borrowing borrowing) throws DAOException {
        String sql = "INSERT INTO borrowings (reader_id, book_id, borrowed_at, due_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, borrowing.getReaderId());
            statement.setObject(2, borrowing.getBookId());
            statement.setTimestamp(3, Timestamp.valueOf(borrowing.getBorrowedAt()));
            statement.setDate(4, Date.valueOf(borrowing.getDueDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error adding borrowing", e);
        }
    }

    @Override
    public void update(Borrowing borrowing) throws DAOException {
        String sql = "UPDATE borrowings SET reader_id = ?, book_id = ?, borrowed_at = ?, due_date = ?, returned_at = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
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
    public Optional<Borrowing> getById(UUID id) throws DAOException {
        String sql = "SELECT id, reader_id, book_id, borrowed_at, due_date, returned_at FROM borrowings WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToOptional(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Error fetching borrowing by ID", e);
        }
    }

    @Override
    public List<Borrowing> getAll() throws DAOException {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT id, reader_id, book_id, borrowed_at, due_date, returned_at FROM borrowings";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
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
    public void delete(UUID id) throws DAOException {
        String sql = "DELETE FROM borrowings WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting borrowing", e);
        }
    }
}