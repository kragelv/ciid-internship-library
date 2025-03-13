package io.kragelv.library.exception;

public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException(String entityName, String fieldValue, String fieldName) {
        super(String.format("%s with %s '%s' not found", entityName, fieldName, fieldValue));
    }
}
