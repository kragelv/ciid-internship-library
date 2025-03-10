package io.kragelv.library.exception;

public class ReaderAlreadyExistsException extends EntityAlreadyExistsException {

    
    public ReaderAlreadyExistsException(String fieldValue, String fieldName) {
        super("Reader", fieldValue, fieldName);
    }
    
}
