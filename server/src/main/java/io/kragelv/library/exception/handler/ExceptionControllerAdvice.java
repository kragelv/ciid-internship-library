package io.kragelv.library.exception.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Conflict;

import io.kragelv.library.exception.ConflictException;
import io.kragelv.library.exception.EntityAlreadyExistsException;
import io.kragelv.library.exception.EntityNotFoundException;
import io.kragelv.library.exception.ResourceNotFoundException;
import io.kragelv.library.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseEntity handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(e -> "Field '" + e.getField() + "' with value '"
                        + e.getRejectedValue() + "' rejected with cause: '" + e.getDefaultMessage() + "'"
                )
                .toList();
        return ErrorResponseEntity
                .builder()
                .message("Validation error: " + String.join("; ", errors))
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponseEntity handleEntityNotFoundException(final EntityNotFoundException ex) {
        return ErrorResponseEntity
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    public ErrorResponseEntity handleConflictException(final ConflictException ex) {
        return ErrorResponseEntity
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponseEntity handleResourceNotFoundException(final ResourceNotFoundException ex) {
        return ErrorResponseEntity
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(ServiceException.class)
    public ErrorResponseEntity handleServiceException(final ServiceException ex) {
        return ErrorResponseEntity
                .builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ErrorResponseEntity handleRuntimeException(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ErrorResponseEntity
                .builder(ex)
                .message("Internal server error occurred")
                .build();
    }

}
