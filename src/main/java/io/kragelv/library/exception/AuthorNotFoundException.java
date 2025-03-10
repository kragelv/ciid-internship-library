package io.kragelv.library.exception;

import java.util.UUID;

public class AuthorNotFoundException extends EntityNotFoundException {

    public AuthorNotFoundException(UUID id) {
        super("Author", id.toString(), "ID");
    }

    public AuthorNotFoundException(String fieldValue, String fieldName) {
        super("Author", fieldValue, fieldName);
    }
}
