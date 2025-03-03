package io.kragelv.library.service.exception;

import java.util.UUID;

public class ReaderNotFoundException extends EntityNotFoundException{

    public ReaderNotFoundException(UUID id) {
        super("Reader", id.toString(), "ID");
    }

    public ReaderNotFoundException(String fieldValue, String fieldName) {
        super("Reader", fieldValue, fieldName);
    }
}
