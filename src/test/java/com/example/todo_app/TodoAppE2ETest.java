// package com.example.todo_app;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.chrome.ChromeDriver;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// public class TodoAppE2ETest {

//     private WebDriver driver;

//     @BeforeEach
//     void setUp() {
//         // Ensure the property is correctly set
//         String chromeDriverPath = System.getProperty("chrome.driver.path");
//         if (chromeDriverPath == null) {
//             throw new IllegalStateException("System property 'chrome.driver.path' is not set.");
//         }
//         System.setProperty("webdriver.chrome.driver", chromeDriverPath);
//         driver = new ChromeDriver();
//     }

//     @Test
//     void testCreateTodo() {
//         driver.get("http://localhost:5173");

//         WebElement titleInput = driver.findElement(By.id("title"));
//         WebElement descriptionInput = driver.findElement(By.id("description"));
//         WebElement submitButton = driver.findElement(By.id("submit"));

//         titleInput.sendKeys("Test Todo");
//         descriptionInput.sendKeys("This is a test todo");
//         submitButton.click();

//         WebElement todoTitle = driver.findElement(By.xpath("//h2[text()='Test Todo']"));
//         assertEquals("Test Todo", todoTitle.getText());
//     }

//     @AfterEach
//     void tearDown() {
//         if (driver != null) {
//             driver.quit();
//         }
//     }
// }
