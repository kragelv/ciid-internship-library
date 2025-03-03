package io.kragelv.library.service.exception;

public class EntityAlreadyExistsException extends ServiceException {

    public EntityAlreadyExistsException(String entityName, String fieldValue, String fieldName) {
        super(String.format("%s with %s '%s' already exists", entityName, fieldName, fieldValue));
    }
}
