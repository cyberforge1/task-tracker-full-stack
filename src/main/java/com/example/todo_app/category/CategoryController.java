package com.example.todo_app.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryDTO data) {
        Category newCategory = this.categoryService.create(data);
        return ResponseEntity.ok(newCategory);
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategories() {
        List<Category> allCategories = this.categoryService.findAll();
        return ResponseEntity.ok(allCategories);
    }
}
