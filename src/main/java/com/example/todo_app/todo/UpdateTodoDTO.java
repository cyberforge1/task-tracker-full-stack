package com.example.todo_app.todo;

import jakarta.validation.constraints.Pattern;

public class UpdateTodoDTO {
    @Pattern(regexp = ".*\\S.*", message = "Title cannot be empty")
    private String title;

    private String description;

    private Boolean completed;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
