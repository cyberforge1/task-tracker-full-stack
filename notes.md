here is some context, no need to answer:
'''
package io.nology.blog.blogpost;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.nology.blog.category.Category;
import io.nology.blog.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "blog_posts")
public class BlogPost extends BaseEntity {

    
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column
    private String title;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "category_id")
   @JsonIgnoreProperties("posts")
   private Category category;

    BlogPost() {}

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

  

    public void setCategory(Category category) {
        this.category = category;
    }

    

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

 

    public Category getCategory() {
        return category;
    }

    

}

'''
'''
package io.nology.blog.blogpost;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.nology.blog.exceptions.NotFoundException;
import io.nology.blog.exceptions.BadRequestException;
import io.nology.blog.exceptions.ServiceValidationException;
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
@RequestMapping("/posts")
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;

    @PostMapping()
    public ResponseEntity<BlogPost> createPost(@Valid @RequestBody CreateBlogPostDTO data) throws BadRequestException {
        BlogPost createdPost;
        try {
            createdPost = this.blogPostService.createPost(data);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            e.printStackTrace();
            throw new BadRequestException(e.generateMessage());
        }
        
    }

    @GetMapping()
    public ResponseEntity<List<BlogPost>> findAllPosts() {
        List<BlogPost> allPosts =  this.blogPostService.findAllPosts();
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> findPostById(@PathVariable Long id) throws NotFoundException {
        Optional<BlogPost> maybePost = this.blogPostService.findById(id);
        BlogPost foundPost = maybePost.orElseThrow(() -> new NotFoundException(BlogPost.class, id));
        return new ResponseEntity<>(foundPost, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BlogPost> updatePostById(@PathVariable Long id,  @Valid @RequestBody UpdateBlogPostDTO data) throws NotFoundException {
        Optional<BlogPost> maybePost = this.blogPostService.updateById(id, data);
        BlogPost updatedPost = maybePost.orElseThrow(() -> new NotFoundException(BlogPost.class, id));
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) throws NotFoundException {
        boolean isDeleted = this.blogPostService.deleteById(id);
        if(!isDeleted) {
            throw new NotFoundException(BlogPost.class, id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}

'''
'''
package io.nology.blog.blogpost;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
'''
'''
package io.nology.blog.blogpost;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.nology.blog.category.Category;
import io.nology.blog.category.CategoryService;
import io.nology.blog.exceptions.NotFoundException;
import io.nology.blog.exceptions.ServiceValidationException;
import io.nology.blog.exceptions.ValidationErrors;
import jakarta.transaction.Transactional;




@Service
@Transactional
public class BlogPostService {
    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    private BlogPostRepository repo;

    @Autowired
    private CategoryService categoryService;

    public BlogPost createPost(CreateBlogPostDTO data) throws ServiceValidationException  {
        // BlogPost newPost = new BlogPost();
        // newPost.setTitle(data.getTitle().trim());
        // newPost.setContent(data.getContent().trim());
        // newPost.setCategory(data.getCategory().trim().toLowerCase());
        BlogPost newPost = mapper.map(data, BlogPost.class);
        ValidationErrors errors = new ValidationErrors();
        Long categoryId = data.getCategoryId();
        Optional<Category> maybeCategory = this.categoryService.findById(categoryId);
        if(maybeCategory.isEmpty()) {
            errors.addError("category", String.format("Category with id %s does not exist", categoryId));
        }else {
            newPost.setCategory(maybeCategory.get());
        }
         if(errors.hasErrors()) {
            throw new ServiceValidationException(errors);
         }
        return this.repo.save(newPost);
    }

    public List<BlogPost> findAllPosts() {
        return this.repo.findAll();
    }

    public Optional<BlogPost> findById(Long id) {
        return this.repo.findById(id);
    }

    public boolean deleteById(Long id) {
        Optional<BlogPost> maybePost = this.findById(id);
        if(maybePost.isEmpty()) {
            return false;
        }
        this.repo.delete(maybePost.get());
       return true; 
    }

    public Optional<BlogPost> updateById(Long id, UpdateBlogPostDTO data) {
        Optional<BlogPost> maybePost = this.findById(id);
        if(maybePost.isEmpty()) {
            return maybePost;
        }

        BlogPost foundPost = maybePost.get();
        // String newTitle = data.getTitle();
        
        // if(newTitle != null) {
        //     foundPost.setTitle(newTitle.trim());
        // }
        // if(data.getCategory() != null) {
        //     foundPost.setCategory(data.getCategory().trim().toLowerCase());
        // }
        // if(data.getContent() != null) {
        //     foundPost.setContent(data.getContent().trim());
        // }
       mapper.map(data, foundPost);
       BlogPost updatedPost = this.repo.save(foundPost);
       return Optional.of(updatedPost);
    }

}

'''
'''
package io.nology.blog.blogpost;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateBlogPostDTO {
    @NotBlank
    private String content;
    @NotBlank
    private String title;
    
    @NotNull
    @Min(1)
    private Long categoryId;
    
    public String getContent() {
        return content;
    }
    public String getTitle() {
        return title;
    }
    public Long getCategoryId() {
        return categoryId;
    }
 
    
}

'''
'''
package io.nology.blog.blogpost;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateBlogPostDTO {
   
    @Pattern(regexp = ".*\\S.*", message = "Content cannot be empty")
    private String content;
    @Pattern(regexp = ".*\\S.*", message = "Title cannot be empty")
    private String title;

    @NotNull
    @Min(1)
    private Long categoryId;
    

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

   

   

    


}

'''
'''
package io.nology.blog.category;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.nology.blog.blogpost.BlogPost;
import io.nology.blog.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties("category")
    private List<BlogPost> posts; 
    
    public Category() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BlogPost> getPosts() {
        return posts;
    }
    
    
}

'''
'''
package io.nology.blog.category;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CreateCategoryDTO data) {
        Category newCategory = this.categoryService.create(data);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Category>> findAllCategories() {
        List<Category> allCategories = this.categoryService.findAll();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }
    
    
}

'''
'''
package io.nology.blog.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

'''
'''
package io.nology.blog.category;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository repo;
    @Autowired
    ModelMapper mapper; 

    public Category create(@Valid CreateCategoryDTO data) {
        Category newCategory = mapper.map(data, Category.class);
        return this.repo.save(newCategory);
    }

    public List<Category> findAll() {
        return this.repo.findAll();
    }

    public Optional<Category> findById(Long id) {
        return this.repo.findById(id);
    }

}

'''
'''
package io.nology.blog.category;

import jakarta.validation.constraints.NotBlank;

public class CreateCategoryDTO {
    @NotBlank
    private String name;

    public CreateCategoryDTO() {
    }

    public String getName() {
        return name;
    }
    
}

'''
'''
package io.nology.blog.common;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column 
    private Date updatedAt;


    
    public BaseEntity() {
    }


    public Long getId() {
        return id;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

  

    public Date getUpdatedAt() {
        return updatedAt;
    }



    @PrePersist
    public void onCreate() {
        Date timestamp = new Date();
        createdAt = timestamp;
        updatedAt = timestamp;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = new Date();
    }
}

'''
'''
package io.nology.blog.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.nology.blog.category.Category;
import io.nology.blog.category.CreateCategoryDTO;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.typeMap(String.class, String.class).setConverter(new StringTrimConverter());
        mapper.typeMap(CreateCategoryDTO.class, Category.class).addMappings(m -> m.using(new LowerCaseConverter()).map(CreateCategoryDTO::getName, Category::setName));
        return mapper;
    }

    private class StringTrimConverter implements Converter<String, String> {

        @Override
        public String convert(MappingContext<String, String> context) {
            if(context.getSource() == null) {
                return null; 
            }
            return context.getSource().trim();
        }
        
    }

    private class LowerCaseConverter implements Converter<String, String> {

        @Override
        public String convert(MappingContext<String, String> context) {
            if(context.getSource() == null) {
                return null; 
            }
            return context.getSource().toLowerCase().trim();
        }
        
    }
}

'''
'''
package io.nology.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = {"http://localhost:5173/", "http://127.0.0.1:5173"};
        registry.addMapping("/**").allowedOrigins(allowedOrigins).allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE").allowedHeaders("*");
    }
}


'''
'''
package io.nology.blog.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends Exception {
    private static final HttpStatus statusCode = HttpStatus.BAD_REQUEST;
    private String message; 
    
    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

    public static HttpStatus getStatuscode() {
        return statusCode;
    }
    public String getMessage() {
        return message;
    }
    


}

'''
'''
package io.nology.blog.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class) 
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), NotFoundException.getStatuscode());
    }

    @ExceptionHandler(BadRequestException.class) 
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), BadRequestException.getStatuscode());
    }
}

'''
'''
package io.nology.blog.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends Exception {
    private static final HttpStatus statusCode = HttpStatus.NOT_FOUND;
    private String message; 
    
    public NotFoundException(Class<?> entityClass, Long id) {
        super(String.format("%s with id %d not found", entityClass.getSimpleName(), id));
        this.message = String.format("%s with id %d not found", entityClass.getSimpleName(), id);
    }

    public static HttpStatus getStatuscode() {
        return statusCode;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}

'''
'''
package io.nology.blog.exceptions;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceValidationException extends Exception {
    private ValidationErrors errors;

    public ServiceValidationException(ValidationErrors errors) {
        super();
        this.errors = errors;
    }

  

    public String generateMessage() {
        Map<String, ArrayList<String>> readOnlyErrors = errors.getErrors();
        // TURN THIS MAP INTO JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(readOnlyErrors);
            return json;
        } catch (JsonProcessingException e) {
            return "Invalid JSON";
        }
    }


}

'''
'''
package io.nology.blog.exceptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidationErrors {
    private HashMap<String, ArrayList<String>> errors;
    

    public void addError(String field, String message) {
        if(this.errors.containsKey(field)) {
            errors.get(field).add(message);
        } else {
            ArrayList<String> newList = new ArrayList<>();
            newList.add(message);
            errors.put(field, newList);
        }
    }

    public ValidationErrors() {
        this.errors = new HashMap<>();
    }

    public boolean isEmpty() {
        return this.errors.isEmpty();
    }

    public boolean hasErrors() {
        return !this.isEmpty();
    }


    public Map<String, ArrayList<String>> getErrors() {
        return Collections.unmodifiableMap(this.errors);
    }
    

}

'''
'''
package io.nology.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}

'''
'''
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.2.5</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>io.nology</groupId>
	<artifactId>blog</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>blog</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
  			<groupId>org.modelmapper</groupId>
  			<artifactId>modelmapper</artifactId>
  			<version>3.0.0</version>
		</dependency>
	</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
				<mainClass>io.nology.blog.BlogApplication</mainClass>
			</configuration>
        </plugin>
    </plugins>
</build>

</project>

'''
ChatGPT
Got it! If you need any help or have any questions related to this context or anything else, feel free to ask.

the above code is from a blog application. I have been tasked to create a todo-app that currently has the following code:
'''
package com.example.todo_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.todo_app.entity.Todo;
import com.example.todo_app.service.TodoService;
import com.example.todo_app.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found for this id :: " + id));
        return ResponseEntity.ok().body(todo);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        Todo updatedTodo = todoService.updateTodo(id, todoDetails);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}

'''
'''
package com.example.todo_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean completed;

    // Default constructor
    public Todo() {
    }

    // Constructor with parameters
    public Todo(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // toString method
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}

'''
'''
package com.example.todo_app.config;

public class SwaggerConfig {
    
}

'''
'''
package com.example.todo_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

'''
'''
package com.example.todo_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

'''
'''
package com.example.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.todo_app.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}

'''
'''
package com.example.todo_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.example.todo_app.entity.Todo;
import com.example.todo_app.repository.TodoRepository;
import com.example.todo_app.exception.ResourceNotFoundException;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, Todo todoDetails) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found for this id :: " + id));
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setCompleted(todoDetails.isCompleted());
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found for this id :: " + id));
        todoRepository.delete(todo);
    }
}

'''
'''
package com.example.todo_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }
}

'''
'''
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>todo-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>todo-app</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <!-- Spring Boot Starter Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Starter Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Spring Boot DevTools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- Spring Boot Starter Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- MySQL Driver -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Swagger for API documentation -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
        </dependency>

        <!-- Jakarta Persistence API -->
        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>3.1.0</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

'''
can you change this todo-app to have the same structure as the blog app and while maintaining full CRUD functionality. Please provide a recommended directory structure also. The database should have a todo table and a category table that are structured the same way as the original blog application.