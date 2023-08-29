package ru.skypro.flea.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.flea.exception.FileSystemError;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.exception.RestError;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(ResourceWithSpecifiedIdNotFoundException.class)
    public ResponseEntity<RestError> handleResourceWithSpecifiedIdNotFoundException(
            ResourceWithSpecifiedIdNotFoundException ex) {
        return configureResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(FileSystemError.class)
    public ResponseEntity<RestError> handleFileSystemError(
            FileSystemError ex) {
        return configureResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<RestError> configureResponse(
            HttpStatus httpStatus,
            Throwable ex) {
        RestError error = new RestError(httpStatus.toString(), ex.getMessage());

        return ResponseEntity
                .status(httpStatus)
                .body(error);
    }

}
