package id.ac.ui.cs.advprog.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EshopApplication {

    private EshopApplication() {
        // Private constructor to prevent instantiation
    }

    public static void main(final String[] args) {
        SpringApplication.run(EshopApplication.class, args);
    }

}