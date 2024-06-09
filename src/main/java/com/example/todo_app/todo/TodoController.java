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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/todos")
@Api(value = "Todo Management System", description = "Operations pertaining to todo in Todo Management System")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping()
    @ApiOperation(value = "Create a new todo", response = Todo.class)
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody CreateTodoDTO data) throws BadRequestException {
        Todo createdTodo;
        try {
            createdTodo = this.todoService.createTodo(data);
            return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            throw new BadRequestException(e.generateMessage());
        }
    }

    @GetMapping()
    @ApiOperation(value = "View a list of available todos", response = List.class)
    public ResponseEntity<List<Todo>> findAllTodos() {
        List<Todo> allTodos = this.todoService.findAllTodos();
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a todo by Id", response = Todo.class)
    public ResponseEntity<Todo> findTodoById(
            @ApiParam(value = "Todo id from which todo object will retrieve", required = true) @PathVariable Long id)
            throws NotFoundException {
        Optional<Todo> maybeTodo = this.todoService.findById(id);
        Todo foundTodo = maybeTodo.orElseThrow(() -> new NotFoundException(Todo.class, id));
        return new ResponseEntity<>(foundTodo, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update a todo by Id", response = Todo.class)
    public ResponseEntity<Todo> updateTodoById(
            @ApiParam(value = "Todo Id to update todo object", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateTodoDTO data) throws NotFoundException {
        Optional<Todo> maybeTodo = this.todoService.updateById(id, data);
        Todo updatedTodo = maybeTodo.orElseThrow(() -> new NotFoundException(Todo.class, id));
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a todo by Id")
    public ResponseEntity<Void> deleteTodoById(
            @ApiParam(value = "Todo Id from which todo object will delete from database table", required = true) @PathVariable Long id)
            throws NotFoundException {
        boolean isDeleted = this.todoService.deleteById(id);
        if (!isDeleted) {
            throw new NotFoundException(Todo.class, id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
