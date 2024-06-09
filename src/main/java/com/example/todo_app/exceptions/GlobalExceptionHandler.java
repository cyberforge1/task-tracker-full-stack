// GlobalExceptionHandler.java

package com.example.todo_app.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        logger.error("NotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), NotFoundException.getStatuscode());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        logger.error("BadRequestException: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), BadRequestException.getStatuscode());
    }

    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<String> handleServiceValidationException(ServiceValidationException ex) {
        String errorMessage = ex.generateMessage();
        logger.error("ServiceValidationException: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
