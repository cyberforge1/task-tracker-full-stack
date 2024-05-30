package com.example.todo_app.category;

import jakarta.validation.constraints.NotBlank;

public class CreateCategoryDTO {

    @NotBlank
    private String name;

    public CreateCategoryDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
