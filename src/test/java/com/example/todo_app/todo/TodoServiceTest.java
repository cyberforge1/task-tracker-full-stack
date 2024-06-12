// src/test/java/com/example/todo_app/todo/TodoServiceTest.java
package com.example.todo_app.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.example.todo_app.todo.CreateTodoDTO;
import com.example.todo_app.todo.Todo;
import com.example.todo_app.todo.TodoRepository;
import com.example.todo_app.todo.TodoService;

public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTodo() throws Exception {
        CreateTodoDTO createTodoDTO = new CreateTodoDTO();
        createTodoDTO.setTitle("Test Todo");
        createTodoDTO.setDescription("This is a test todo");

        Todo todo = new Todo();
        todo.setTitle(createTodoDTO.getTitle());
        todo.setDescription(createTodoDTO.getDescription());

        when(modelMapper.map(createTodoDTO, Todo.class)).thenReturn(todo);
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo createdTodo = todoService.createTodo(createTodoDTO);

        assertEquals("Test Todo", createdTodo.getTitle());
        assertEquals("This is a test todo", createdTodo.getDescription());
    }

    @Test
    void testFindById() throws Exception {
        Long id = 1L;
        Todo todo = new Todo();
        todo.setId(id);
        todo.setTitle("Test Todo");

        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        Optional<Todo> foundTodo = todoService.findById(id);

        assertEquals("Test Todo", foundTodo.get().getTitle());
    }
}
