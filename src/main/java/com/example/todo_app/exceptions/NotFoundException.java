// NotFoundException.java

package com.example.todo_app.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends Exception {
    private static final HttpStatus statusCode = HttpStatus.NOT_FOUND;
    private String message;

    public NotFoundException(Class<?> entityClass, Long id) {
        super(String.format("%s with id %d not found", entityClass.getSimpleName(), id));
        this.message = String.format("%s with id %d not found", entityClass.getSimpleName(), id);
    }

    public static HttpStatus getStatuscode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
