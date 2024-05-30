package com.example.todo_app.category;

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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private List<Category> categoryList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryList = new ArrayList<>();
        Category category1 = new Category();
        category1.setName("Work");
        Category category2 = new Category();
        category2.setName("Personal");

        categoryList.add(category1);
        categoryList.add(category2);
    }

    @Test
    public void shouldFetchAllCategories() throws Exception {
        when(categoryService.findAll()).thenReturn(categoryList);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(categoryList.get(0).getName())));
    }

    @Test
    public void shouldCreateNewCategory() throws Exception {
        Category category = categoryList.get(0);
        CreateCategoryDTO dto = new CreateCategoryDTO();
        dto.setName(category.getName());

        when(categoryService.create(any(CreateCategoryDTO.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(category.getName())));
    }
}
