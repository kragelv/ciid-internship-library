package io.kragelv.library.exception;

import java.time.Instant;
import java.util.UUID;

public class ReturnedBorrowingModificationException extends ConflictException {

    public ReturnedBorrowingModificationException(UUID id, Instant returnedAt) {
        super(String.format("Cannot update borrowing record with ID '%s' because it has already been returned at %s", id.toString(), returnedAt));
    }

}