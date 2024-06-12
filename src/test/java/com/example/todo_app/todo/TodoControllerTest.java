// // src/test/java/com/example/todo_app/todo/TodoControllerTest.java
// package com.example.todo_app.todo;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class TodoControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     void testCreateTodo() throws Exception {
//         String todoJson = "{\"title\":\"Test Todo\",\"description\":\"This is a test todo\"}";

//         mockMvc.perform(post("/todos")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(todoJson))
//                 .andExpect(status().isCreated());
//     }

//     @Test
//     void testGetTodoById() throws Exception {
//         // Assuming a todo with ID 1 exists
//         mockMvc.perform(get("/todos/1"))
//                 .andExpect(status().isOk());
//     }
// }
