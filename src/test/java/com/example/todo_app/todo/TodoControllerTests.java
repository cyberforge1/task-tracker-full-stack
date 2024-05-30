package com.example.todo_app.todo;

import com.example.todo_app.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private List<Todo> todoList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        todoList = new ArrayList<>();
        Category category = new Category();
        category.setName("Work");

        Todo todo1 = new Todo("Title 1", "Description 1", false);
        todo1.setCategory(category);
        Todo todo2 = new Todo("Title 2", "Description 2", true);
        todo2.setCategory(category);

        todoList.add(todo1);
        todoList.add(todo2);
    }

    @Test
    public void shouldFetchAllTodos() throws Exception {
        when(todoService.findAllTodos()).thenReturn(todoList);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(todoList.get(0).getTitle())));
    }

    @Test
    public void shouldFindTodoById() throws Exception {
        Todo todo = todoList.get(0);
        when(todoService.findById(anyLong())).thenReturn(Optional.of(todo));

        mockMvc.perform(get("/api/todos/{id}", todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(todo.getTitle())));
    }

    @Test
    public void shouldReturn404WhenFetchingNonExistingTodo() throws Exception {
        when(todoService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewTodo() throws Exception {
        Todo todo = todoList.get(0);
        CreateTodoDTO dto = new CreateTodoDTO();
        dto.setCategoryId(todo.getCategory().getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCompleted(todo.isCompleted());

        when(todoService.createTodo(any(CreateTodoDTO.class))).thenReturn(todo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Todo\", \"description\": \"New Description\", \"completed\": false, \"categoryId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(todo.getTitle())));
    }

    @Test
    public void shouldUpdateTodo() throws Exception {
        Todo todo = todoList.get(0);
        UpdateTodoDTO dto = new UpdateTodoDTO();
        dto.setCategoryId(todo.getCategory().getId());
        dto.setTitle("Updated Title");
        dto.setDescription(todo.getDescription());
        dto.setCompleted(todo.isCompleted());

        when(todoService.updateById(anyLong(), any(UpdateTodoDTO.class))).thenReturn(Optional.of(todo));

        mockMvc.perform(patch("/api/todos/{id}", todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Updated Title\", \"description\": \"Updated Description\", \"completed\": true, \"categoryId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(todo.getTitle())));
    }

    @Test
    public void shouldDeleteTodo() throws Exception {
        Todo todo = todoList.get(0);
        when(todoService.deleteById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/todos/{id}", todo.getId()))
                .andExpect(status().isNoContent());
    }
}
