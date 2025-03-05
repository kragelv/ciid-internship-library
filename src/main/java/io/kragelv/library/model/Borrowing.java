package io.kragelv.library.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Borrowing {

    private UUID id;
    private UUID readerId;
    private UUID bookId;
    private LocalDateTime borrowedAt;
    private LocalDate dueDate;
    private LocalDateTime returnedAt;

    public Borrowing() { }

    public Borrowing(UUID id, UUID readerId, UUID bookId, LocalDateTime borrowedAt, LocalDate dueDate,
            LocalDateTime returnedAt) {
        this.id = id;
        this.readerId = readerId;
        this.bookId = bookId;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getReaderId() {
        return readerId;
    }

    public void setReaderId(UUID readerId) {
        this.readerId = readerId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDateTime borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}
