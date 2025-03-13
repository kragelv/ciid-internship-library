package io.kragelv.library.exception;

import java.util.UUID;

public class GenreNotFoundException extends EntityNotFoundException {

    public GenreNotFoundException(UUID id) {
        super("Genre", id.toString(), "ID");
    }

    public GenreNotFoundException(String fieldValue, String fieldName) {
        super("Genre", fieldValue, fieldName);
    }
}
