package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import  static org.junit.jupiter.api.Assertions.*;
class ProductTest {
    Product product;
    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.product.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        this.product.setProductName("Laptop");
        this.product.setProductQuantity(12);
    }

    @Test
    void testGetProductId() {
        assertEquals("7b3961ae-953c-43d5-8825-b778e35d0982", this.product.getProductId());
    }

    @Test
    void testGetProductName() {
        assertEquals("Laptop", this.product.getProductName());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(12, this.product.getProductQuantity());
    }
}