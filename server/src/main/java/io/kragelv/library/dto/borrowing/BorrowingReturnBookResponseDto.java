package io.kragelv.library.dto.borrowing;

import java.time.Instant;

public record BorrowingReturnBookResponseDto(
        Instant returnedAt
        ) {

}
