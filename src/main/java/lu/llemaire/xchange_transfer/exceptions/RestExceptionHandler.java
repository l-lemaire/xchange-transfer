package lu.llemaire.xchange_transfer.exceptions;

import lu.llemaire.xchange_transfer.common.ErrorEntity;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * <p>This class is used to handle exceptions thrown by the REST API</p>
 *
 * </p>It is used in case of an unexpected exception, it will return a 500 error code with a correct body</p>
 *
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorEntity> handleNotFoundException(NotFoundException e) {
        return ResponseEntity
                .status(404).
                body(buildErrorEntity(e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity
                .status(409)
                .body(buildErrorEntity(e.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorEntity> handleException(Throwable e) {
        return ResponseEntity
                .status(500)
                .body(buildErrorEntity(e.getMessage()));
    }

    protected ErrorEntity buildErrorEntity(String message) {
        return ErrorEntity.builder()
                .uuid(ThreadContext.get("requestId"))
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
