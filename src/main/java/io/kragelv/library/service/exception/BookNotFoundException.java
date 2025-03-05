package io.kragelv.library.service.exception;

import java.util.UUID;

public class BookNotFoundException extends EntityNotFoundException {

    public BookNotFoundException(UUID id) {
        super("Book", id.toString(), "ID");
    }

    public BookNotFoundException(String fieldValue, String fieldName) {
        super("Book", fieldValue, fieldName);
    }

}
