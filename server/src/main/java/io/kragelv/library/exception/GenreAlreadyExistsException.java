package io.kragelv.library.exception;

public class GenreAlreadyExistsException extends EntityAlreadyExistsException {

    public GenreAlreadyExistsException(String fieldValue, String fieldName) {
        super("Genre", fieldValue, fieldName);
    }
    
}
