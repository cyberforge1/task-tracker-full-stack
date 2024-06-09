// TodoController.java

package com.example.todo_app.todo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo_app.exceptions.BadRequestException;
import com.example.todo_app.exceptions.NotFoundException;
import com.example.todo_app.exceptions.ServiceValidationException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping()
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody CreateTodoDTO data) throws BadRequestException {
        Todo createdTodo;
        try {
            createdTodo = this.todoService.createTodo(data);
            return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            e.printStackTrace();
            throw new BadRequestException(e.generateMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<Todo>> findAllTodos() {
        List<Todo> allTodos = this.todoService.findAllTodos();
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findTodoById(@PathVariable Long id) throws NotFoundException {
        Optional<Todo> maybeTodo = this.todoService.findById(id);
        Todo foundTodo = maybeTodo.orElseThrow(() -> new NotFoundException(Todo.class, id));
        return new ResponseEntity<>(foundTodo, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodoById(@PathVariable Long id, @Valid @RequestBody UpdateTodoDTO data) throws NotFoundException {
        Optional<Todo> maybeTodo = this.todoService.updateById(id, data);
        Todo updatedTodo = maybeTodo.orElseThrow(() -> new NotFoundException(Todo.class, id));
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) throws NotFoundException {
        boolean isDeleted = this.todoService.deleteById(id);
        if (!isDeleted) {
            throw new NotFoundException(Todo.class, id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
