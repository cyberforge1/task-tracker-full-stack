package com.example.todo_app.todo;

import com.example.todo_app.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.findAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found for this id :: " + id));
        return ResponseEntity.ok().body(todo);
    }

    @PostMapping
    public Todo createTodo(@RequestBody CreateTodoDTO createTodoDTO) {
        return todoService.createTodo(createTodoDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody UpdateTodoDTO data) {
        Todo updatedTodo = todoService.updateById(id, data).orElseThrow(() -> new ResourceNotFoundException("Todo not found for this id :: " + id));
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) {
        boolean isDeleted = todoService.deleteById(id);
        if (!isDeleted) {
            throw new ResourceNotFoundException("Todo not found for this id :: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
