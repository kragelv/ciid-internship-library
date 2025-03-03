package io.kragelv.library.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.kragelv.library.model.Borrowing;

public interface BorrowingService {

    Borrowing borrowBook(UUID readerId, UUID bookId, LocalDate dueDate);

    Borrowing borrowBook(UUID readerId, UUID bookId, LocalDate dueDate, LocalDateTime borrowedAt);

    void returnBook(UUID borrowingId);

    List<Borrowing> getAllBorrowings();

    Borrowing getBorrowingById(UUID borrowingId);

    List<Borrowing> getBorrowingsByReader(UUID readerId);

    List<Borrowing> getBorrowingsByBook(UUID bookId);
}

