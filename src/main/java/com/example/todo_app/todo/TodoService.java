package com.example.todo_app.todo;

import com.example.todo_app.category.Category;
import com.example.todo_app.category.CategoryService;
import com.example.todo_app.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TodoRepository repo;

    @Autowired
    private CategoryService categoryService;

    public Todo createTodo(CreateTodoDTO data) {
        Todo newTodo = mapper.map(data, Todo.class);
        Long categoryId = data.getCategoryId();
        Optional<Category> maybeCategory = categoryService.findById(categoryId);
        if (maybeCategory.isPresent()) {
            newTodo.setCategory(maybeCategory.get());
        } else {
            throw new ResourceNotFoundException("Category not found for this id :: " + categoryId);
        }
        return repo.save(newTodo);
    }

    public List<Todo> findAllTodos() {
        return repo.findAll();
    }

    public Optional<Todo> findById(Long id) {
        return repo.findById(id);
    }

    public boolean deleteById(Long id) {
        Optional<Todo> maybeTodo = findById(id);
        if (maybeTodo.isEmpty()) {
            return false;
        }
        repo.delete(maybeTodo.get());
        return true;
    }

    public Optional<Todo> updateById(Long id, UpdateTodoDTO data) {
        Optional<Todo> maybeTodo = findById(id);
        if (maybeTodo.isEmpty()) {
            return maybeTodo;
        }
        Todo foundTodo = maybeTodo.get();
        mapper.map(data, foundTodo);
        Todo updatedTodo = repo.save(foundTodo);
        return Optional.of(updatedTodo);
    }
}
