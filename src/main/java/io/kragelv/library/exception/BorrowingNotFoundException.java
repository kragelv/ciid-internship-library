package io.kragelv.library.exception;

import java.util.UUID;

public class BorrowingNotFoundException extends EntityNotFoundException {

    public BorrowingNotFoundException(UUID id) {
        super("Borrowing", id.toString(), "ID");
    }

    public BorrowingNotFoundException(String fieldValue, String fieldName) {
        super("Borrowing", fieldValue, fieldName);
    }
    
}
