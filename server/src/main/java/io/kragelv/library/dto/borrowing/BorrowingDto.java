package io.kragelv.library.dto.borrowing;

import java.time.Instant;
import java.time.LocalDate;

public record BorrowingDto(
        String id,
        ReaderDto reader,
        BookDto book,
        Instant borrowedAt,
        LocalDate dueDate,
        Instant returnedAt) {

    public record ReaderDto(
            String id,
            String fullname,
            String email) {
    }

    public record BookDto(
            String id,
            String title,
            AuthorDto author,
            Integer publishedYear) {

        public record AuthorDto(
                String id,
                String firstName,
                String middleName,
                String lastName) {
        }
    }
}
