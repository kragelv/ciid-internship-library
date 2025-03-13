package io.kragelv.library.exception;

import java.time.Instant;
import java.util.UUID;

public class BorrowingAlreadyReturnedException extends ConflictException {

    public BorrowingAlreadyReturnedException(UUID id, Instant returnedAt) {
        super(String.format("Borrowing with ID '%s' has already been returned at %s", id.toString(), returnedAt));
    }

}
