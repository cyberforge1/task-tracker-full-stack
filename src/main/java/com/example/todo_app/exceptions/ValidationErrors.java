// ValidationErrors.java

package com.example.todo_app.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidationErrors {
    private static final Logger logger = LoggerFactory.getLogger(ValidationErrors.class);
    private HashMap<String, ArrayList<String>> errors;

    public ValidationErrors() {
        this.errors = new HashMap<>();
    }

    public void addError(String field, String message) {
        if (this.errors.containsKey(field)) {
            logger.debug("Adding additional error for field {}: {}", field, message);
            errors.get(field).add(message);
        } else {
            logger.debug("Adding new error for field {}: {}", field, message);
            ArrayList<String> newList = new ArrayList<>();
            newList.add(message);
            errors.put(field, newList);
        }
    }

    public boolean isEmpty() {
        return this.errors.isEmpty();
    }

    public boolean hasErrors() {
        return !this.isEmpty();
    }

    public Map<String, ArrayList<String>> getErrors() {
        return Collections.unmodifiableMap(this.errors);
    }
}
