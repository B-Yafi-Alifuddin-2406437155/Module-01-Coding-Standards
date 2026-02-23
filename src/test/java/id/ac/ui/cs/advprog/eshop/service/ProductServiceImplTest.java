package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product.setProductName("Laptop");
        product.setProductQuantity(12);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.create(product)).thenReturn(product);

        Product createdProduct = productService.create(product);

        assertNotNull(createdProduct);
        assertEquals("Laptop", createdProduct.getProductName());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        Product product2 = new Product();
        product2.setProductId("8c4973ae-123c-43d5-8825-b778e35d5678");
        product2.setProductName("Smartphone");
        product2.setProductQuantity(20);
        productList.add(product2);

        Iterator<Product> productIterator = productList.iterator();
        when(productRepository.findAll()).thenReturn(productIterator);

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        assertEquals("Smartphone", result.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWhenEmpty() {
        List<Product> emptyList = new ArrayList<>();
        Iterator<Product> emptyIterator = emptyList.iterator();
        when(productRepository.findAll()).thenReturn(emptyIterator);

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdPositive() {
        when(productRepository.findById("7b3961ae-953c-43d5-8825-b778e35d0982")).thenReturn(product);

        Product foundProduct = productService.findById("7b3961ae-953c-43d5-8825-b778e35d0982");

        assertNotNull(foundProduct);
        assertEquals("Laptop", foundProduct.getProductName());
        assertEquals(12, foundProduct.getProductQuantity());
        verify(productRepository, times(1)).findById("7b3961ae-953c-43d5-8825-b778e35d0982");
    }

    @Test
    void testFindByIdNegativeNotFound() {
        when(productRepository.findById("non-existent-id")).thenReturn(null);

        Product foundProduct = productService.findById("non-existent-id");

        assertNull(foundProduct);
        verify(productRepository, times(1)).findById("non-existent-id");
    }

    @Test
    void testUpdateProduct() {
        doNothing().when(productRepository).update(product);

        productService.update(product);

        verify(productRepository, times(1)).update(product);
    }

    @Test
    void testDeleteById() {
        doNothing().when(productRepository).deleteById("7b3961ae-953c-43d5-8825-b778e35d0982");

        productService.deleteById("7b3961ae-953c-43d5-8825-b778e35d0982");

        verify(productRepository, times(1)).deleteById("7b3961ae-953c-43d5-8825-b778e35d0982");
    }
}