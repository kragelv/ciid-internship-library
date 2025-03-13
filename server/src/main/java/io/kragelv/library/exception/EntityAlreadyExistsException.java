package io.kragelv.library.exception;

public class EntityAlreadyExistsException extends ConflictException {

    public EntityAlreadyExistsException(String entityName, String fieldValue, String fieldName) {
        super(String.format("%s with %s '%s' already exists", entityName, fieldName, fieldValue));
    }
}
