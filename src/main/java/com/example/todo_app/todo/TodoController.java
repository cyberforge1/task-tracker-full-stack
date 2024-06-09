// TodoService.java

package com.example.todo_app.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @PostMapping()
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody CreateTodoDTO data) throws BadRequestException {
        logger.info("Request to create a new todo with title: {}", data.getTitle());
        Todo createdTodo;
        try {
            createdTodo = this.todoService.createTodo(data);
            logger.info("Successfully created todo with ID: {}", createdTodo.getId());
            return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            logger.error("Validation error when creating todo: {}", e.getMessage());
            throw new BadRequestException(e.generateMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<Todo>> findAllTodos() {
        logger.info("Request to find all todos");
        List<Todo> allTodos = this.todoService.findAllTodos();
        logger.info("Found {} todos", allTodos.size());
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findTodoById(@PathVariable Long id) throws NotFoundException {
        logger.info("Request to find todo by ID: {}", id);
        Optional<Todo> maybeTodo = this.todoService.findById(id);
        Todo foundTodo = maybeTodo.orElseThrow(() -> new NotFoundException(Todo.class, id));
        logger.info("Found todo with ID: {}", foundTodo.getId());
        return new ResponseEntity<>(foundTodo, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodoById(@PathVariable Long id, @Valid @RequestBody UpdateTodoDTO data) throws NotFoundException {
        logger.info("Request to update todo with ID: {}", id);
        Optional<Todo> maybeTodo = this.todoService.updateById(id, data);
        Todo updatedTodo = maybeTodo.orElseThrow(() -> new NotFoundException(Todo.class, id));
        logger.info("Successfully updated todo with ID: {}", updatedTodo.getId());
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) throws NotFoundException {
        logger.info("Request to delete todo with ID: {}", id);
        boolean isDeleted = this.todoService.deleteById(id);
        if (!isDeleted) {
            logger.error("Todo with ID: {} not found", id);
            throw new NotFoundException(Todo.class, id);
        }
        logger.info("Successfully deleted todo with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
