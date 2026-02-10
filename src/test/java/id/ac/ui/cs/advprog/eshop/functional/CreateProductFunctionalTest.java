package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isSuccessful(ChromeDriver driver) throws Exception {
        // Navigate to Create Product page
        driver.get(baseUrl + "/product/create");

        // Verify page title
        String pageTitle = driver.getTitle();
        assertEquals("Create New Product", pageTitle);

        // Fill in the product name
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.clear();
        nameInput.sendKeys("Test Product");

        // Fill in the product quantity
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.clear();
        quantityInput.sendKeys("100");

        // Click the Submit button
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify redirect to product list page
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/product/list"));

        // Verify the product list page title
        String listPageTitle = driver.getTitle();
        assertEquals("Product List", listPageTitle);

        // Verify the new product appears in the list
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Test Product"));
        assertTrue(pageSource.contains("100"));
    }

    @Test
    void createProduct_multipleProducts_allAppearInList(ChromeDriver driver) throws Exception {
        // Create first product
        driver.get(baseUrl + "/product/create");

        WebElement nameInput1 = driver.findElement(By.id("nameInput"));
        nameInput1.clear();
        nameInput1.sendKeys("Product Alpha");

        WebElement quantityInput1 = driver.findElement(By.id("quantityInput"));
        quantityInput1.clear();
        quantityInput1.sendKeys("50");

        WebElement submitButton1 = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton1.click();

        // Create second product
        driver.get(baseUrl + "/product/create");

        WebElement nameInput2 = driver.findElement(By.id("nameInput"));
        nameInput2.clear();
        nameInput2.sendKeys("Product Beta");

        WebElement quantityInput2 = driver.findElement(By.id("quantityInput"));
        quantityInput2.clear();
        quantityInput2.sendKeys("75");

        WebElement submitButton2 = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton2.click();

        // Verify both products appear in the list
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Product Alpha"));
        assertTrue(pageSource.contains("50"));
        assertTrue(pageSource.contains("Product Beta"));
        assertTrue(pageSource.contains("75"));
    }

    @Test
    void createProductPage_hasCorrectFormElements(ChromeDriver driver) throws Exception {
        // Navigate to Create Product page
        driver.get(baseUrl + "/product/create");

        // Verify form elements exist
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        assertEquals("text", nameInput.getAttribute("type"));

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        assertEquals("text", quantityInput.getAttribute("type"));

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertEquals("Submit", submitButton.getText());

        // Verify labels
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Name"));
        assertTrue(pageSource.contains("Quantity"));
    }
}