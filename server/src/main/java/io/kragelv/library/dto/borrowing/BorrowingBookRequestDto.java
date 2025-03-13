package io.kragelv.library.dto.borrowing;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record BorrowingBookRequestDto(

    @NotNull
    UUID readerId,

    @NotNull
    UUID bookId,

    @NotNull
    LocalDate dueDate
) {
    
}
