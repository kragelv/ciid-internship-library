package io.kragelv.library.dao.exception;

public class PropertiesLoadException extends RuntimeException{
    
    public PropertiesLoadException(String message) {
        super(message);
    }

    public PropertiesLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertiesLoadException(Throwable cause) {
        super(cause);
    }
}
