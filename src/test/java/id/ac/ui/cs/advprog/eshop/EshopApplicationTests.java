package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EshopApplicationTest {

    @Test
    void contextLoads() {
        // Verifies that the Spring context loads successfully
        assertTrue(true, "Spring context should load successfully");
    }

    @Test
    void mainMethodShouldStartApplication() {
        assertDoesNotThrow(() -> {
            EshopApplication.main(new String[]{});
        }, "Main method should start application without throwing exception");
    }
}