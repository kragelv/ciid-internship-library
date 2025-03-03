package io.kragelv.library.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.kragelv.library.config.DAO;
import io.kragelv.library.dao.BookDAO;
import io.kragelv.library.dao.BorrowingDAO;
import io.kragelv.library.dao.DAOFactory;
import io.kragelv.library.dao.ReaderDAO;
import io.kragelv.library.model.Book;
import io.kragelv.library.model.Borrowing;
import io.kragelv.library.model.Reader;
import io.kragelv.library.service.BorrowingService;
import io.kragelv.library.service.exception.BorrowingNotFoundException;
import io.kragelv.library.service.exception.ReaderNotFoundException;

public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingDAO borrowingDAO;
    private final ReaderDAO readerDAO;
    private final BookDAO bookDAO;

    public BorrowingServiceImpl() {
        DAOFactory daoFactory = DAO.getFactory();
        this.borrowingDAO = daoFactory.getBorrowingDAO();
        this.readerDAO = daoFactory.getReaderDAO();
        this.bookDAO = daoFactory.getBookDAO();
    }

    public BorrowingServiceImpl(BorrowingDAO borrowingDAO, ReaderDAO readerDAO, BookDAO bookDAO) {
        this.borrowingDAO = borrowingDAO;
        this.readerDAO = readerDAO;
        this.bookDAO = bookDAO;
    }

    @Override
    public Borrowing borrowBook(UUID readerId, UUID bookId, LocalDate dueDate, LocalDateTime borrowedAt) {
        Optional<Reader> reader = readerDAO.getById(readerId);
        if (reader.isEmpty()) {
            throw new ReaderNotFoundException(bookId);
        }

        Optional<Book> book = bookDAO.getById(bookId);
        if (book.isEmpty()) {
            throw new ReaderNotFoundException(bookId);
        }

        Borrowing borrowing = new Borrowing();
        borrowing.setReaderId(readerId);
        borrowing.setBookId(bookId);
        borrowing.setBorrowedAt(borrowedAt);
        borrowing.setDueDate(dueDate);

        return borrowingDAO.create(borrowing);
    }

    @Override
    public Borrowing borrowBook(UUID readerId, UUID bookId, LocalDate dueDate) {
        return borrowBook(readerId, bookId, dueDate, LocalDateTime.now());
    }

    @Override
    public void returnBook(UUID borrowingId) {
        Optional<Borrowing> borrowing = borrowingDAO.getById(borrowingId);
        if (borrowing.isEmpty()) {
            throw new BorrowingNotFoundException(borrowingId);
        }

        Borrowing updatedBorrowing = borrowing.get();
        updatedBorrowing.setReturnedAt(LocalDateTime.now());

        borrowingDAO.update(updatedBorrowing);
    }

    @Override
    public List<Borrowing> getAllBorrowings() {
        return borrowingDAO.getAll();
    }

    @Override
    public Borrowing getBorrowingById(UUID borrowingId) {
        return borrowingDAO.getById(borrowingId)
                .orElseThrow(() -> new BorrowingNotFoundException(borrowingId));
    }

    @Override
    public List<Borrowing> getBorrowingsByReader(UUID readerId) {
        return borrowingDAO.getByReaderId(readerId);
    }

    @Override
    public List<Borrowing> getBorrowingsByBook(UUID bookId) {
        return borrowingDAO.getByBookId(bookId);
    }
}

