// TodoService.java

package com.example.todo_app.todo;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo_app.exceptions.NotFoundException;
import com.example.todo_app.exceptions.ServiceValidationException;
import com.example.todo_app.exceptions.ValidationErrors;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {
    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TodoRepository repo;

    public Todo createTodo(CreateTodoDTO data) throws ServiceValidationException {
        logger.debug("Creating new Todo with title: {}", data.getTitle());
        Todo newTodo = mapper.map(data, Todo.class);
        ValidationErrors errors = new ValidationErrors();
        if (errors.hasErrors()) {
            logger.warn("Validation errors: {}", errors);
            throw new ServiceValidationException(errors);
        }
        Todo savedTodo = repo.save(newTodo);
        logger.debug("Saved new Todo with ID: {}", savedTodo.getId());
        return savedTodo;
    }

    public List<Todo> findAllTodos() {
        logger.debug("Finding all Todos");
        List<Todo> todos = repo.findAll();
        logger.debug("Found {} Todos", todos.size());
        return todos;
    }

    public Optional<Todo> findById(Long id) {
        logger.debug("Finding Todo by ID: {}", id);
        return repo.findById(id);
    }

    public boolean deleteById(Long id) {
        logger.debug("Deleting Todo by ID: {}", id);
        Optional<Todo> maybeTodo = this.findById(id);
        if (maybeTodo.isEmpty()) {
            logger.warn("Todo with ID: {} not found", id);
            return false;
        }
        repo.delete(maybeTodo.get());
        logger.debug("Deleted Todo with ID: {}", id);
        return true;
    }

    public Optional<Todo> updateById(Long id, UpdateTodoDTO data) {
        logger.debug("Updating Todo by ID: {}", id);
        Optional<Todo> maybeTodo = this.findById(id);
        if (maybeTodo.isEmpty()) {
            logger.warn("Todo with ID: {} not found", id);
            return Optional.empty();
        }
        Todo foundTodo = maybeTodo.get();
        mapper.map(data, foundTodo);
        Todo updatedTodo = repo.save(foundTodo);
        logger.debug("Updated Todo with ID: {}", updatedTodo.getId());
        return Optional.of(updatedTodo);
    }
}
