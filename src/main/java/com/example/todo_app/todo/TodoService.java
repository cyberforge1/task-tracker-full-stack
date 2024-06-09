// TodoService.java

package com.example.todo_app.todo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo_app.exceptions.NotFoundException;
import com.example.todo_app.exceptions.ServiceValidationException;
import com.example.todo_app.exceptions.ValidationErrors;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TodoService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TodoRepository repo;

    public Todo createTodo(CreateTodoDTO data) throws ServiceValidationException {
        Todo newTodo = mapper.map(data, Todo.class);
        ValidationErrors errors = new ValidationErrors();
        if (errors.hasErrors()) {
            throw new ServiceValidationException(errors);
        }
        return this.repo.save(newTodo);
    }

    public List<Todo> findAllTodos() {
        return this.repo.findAll();
    }

    public Optional<Todo> findById(Long id) {
        return this.repo.findById(id);
    }

    public boolean deleteById(Long id) {
        Optional<Todo> maybeTodo = this.findById(id);
        if (maybeTodo.isEmpty()) {
            return false;
        }
        this.repo.delete(maybeTodo.get());
        return true;
    }

    public Optional<Todo> updateById(Long id, UpdateTodoDTO data) {
        Optional<Todo> maybeTodo = this.findById(id);
        if (maybeTodo.isEmpty()) {
            return maybeTodo;
        }

        Todo foundTodo = maybeTodo.get();
        mapper.map(data, foundTodo);
        Todo updatedTodo = this.repo.save(foundTodo);
        return Optional.of(updatedTodo);
    }
}
