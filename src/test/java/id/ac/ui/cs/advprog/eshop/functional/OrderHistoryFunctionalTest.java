package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderHistoryFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void orderHistory_showsOrdersForAuthor(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys("Safira");
        driver.findElement(By.id("productNameInput")).sendKeys("Laptop");
        driver.findElement(By.id("productQuantityInput")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl + "/order/history");
        driver.findElement(By.id("authorInput")).sendKeys("Safira");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Safira"));
    }
}