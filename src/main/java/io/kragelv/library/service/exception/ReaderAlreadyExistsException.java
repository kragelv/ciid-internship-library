package io.kragelv.library.service.exception;

public class ReaderAlreadyExistsException extends EntityAlreadyExistsException {

    
    public ReaderAlreadyExistsException(String fieldValue, String fieldName) {
        super("Reader", fieldValue, fieldName);
    }
    
}
