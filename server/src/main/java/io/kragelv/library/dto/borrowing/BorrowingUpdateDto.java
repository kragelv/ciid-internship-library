package io.kragelv.library.dto.borrowing;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record BorrowingUpdateDto(

    @NotNull
    LocalDate dueDate
) {
    
}
