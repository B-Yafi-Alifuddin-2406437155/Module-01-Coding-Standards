package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;
    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateAndFind(){
        Product product = new Product();
        product.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product.setProductName("Laptop");
        product.setProductQuantity(12);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testCreateWithNullProductId() {
        Product product = new Product();
        product.setProductId(null);  // Branch: null == true
        product.setProductName("Test");

        Product result = productRepository.create(product);

        assertNotNull(result.getProductId());  // UUID baru dibuat
    }

    @Test
    void testCreateWithExistingProductId() {
        Product product = new Product();
        product.setProductId("existing-id");  // Branch: null == false, isEmpty() == false
        product.setProductName("Test");

        Product result = productRepository.create(product);

        assertEquals("existing-id", result.getProductId());  // UUID tidak berubah
    }

    @Test
    void testFindAllIfEmpty(){
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllMoreThanOneProduct(){
        Product product1 = new Product();
        product1.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product1.setProductName("Laptop");
        product1.setProductQuantity(12);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("8c4973ae-123c-43d5-8825-b778e35d5678");
        product2.setProductName("Smartphone");
        product2.setProductQuantity(20);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testCreateWithEmptyProductId() {
        Product product = new Product();
        product.setProductId("");
        product.setProductName("Smartphone");
        product.setProductQuantity(5);

        Product createdProduct = productRepository.create(product);

        assertNotNull(createdProduct.getProductId());
        assertFalse(createdProduct.getProductId().isEmpty());
        Product savedProduct = productRepository.findById(createdProduct.getProductId());
        assertNotNull(savedProduct);
        assertEquals("Smartphone", savedProduct.getProductName());
    }

    @Test
    void testUpdateProductPositive() {
        Product product = new Product();
        product.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product.setProductName("Laptop");
        product.setProductQuantity(12);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        updatedProduct.setProductName("Gaming Laptop");
        updatedProduct.setProductQuantity(5);

        productRepository.update(updatedProduct);

        Product savedProduct = productRepository.findById("7b3961ae-953c-43d5-8825-b778e35d0982");
        assertNotNull(savedProduct);
        assertEquals("Gaming Laptop", savedProduct.getProductName());
        assertEquals(5, savedProduct.getProductQuantity());
    }

    @Test
    void testUpdateProductNegativeNonExistent() {
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Non Existent Product");
        nonExistentProduct.setProductQuantity(10);

        productRepository.update(nonExistentProduct);

        Product result = productRepository.findById("non-existent-id");
        assertNull(result);
    }

    @Test
    void testDeleteProductPositive() {
        Product product = new Product();
        product.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product.setProductName("Laptop");
        product.setProductQuantity(12);
        productRepository.create(product);

        productRepository.deleteById("7b3961ae-953c-43d5-8825-b778e35d0982");

        Product deletedProduct = productRepository.findById("7b3961ae-953c-43d5-8825-b778e35d0982");
        assertNull(deletedProduct);
    }

    @Test
    void testDeleteProductNegativeNonExistent() {
        productRepository.deleteById("non-existent-id");

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductOnlyDeletesTargetProduct() {
        Product product1 = new Product();
        product1.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product1.setProductName("Laptop");
        product1.setProductQuantity(12);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("8c4973ae-123c-43d5-8825-b778e35d5678");
        product2.setProductName("Smartphone");
        product2.setProductQuantity(20);
        productRepository.create(product2);

        productRepository.deleteById("7b3961ae-953c-43d5-8825-b778e35d0982");

        Product deletedProduct = productRepository.findById("7b3961ae-953c-43d5-8825-b778e35d0982");
        assertNull(deletedProduct);

        Product remainingProduct = productRepository.findById("8c4973ae-123c-43d5-8825-b778e35d5678");
        assertNotNull(remainingProduct);
        assertEquals("Smartphone", remainingProduct.getProductName());
    }

    @Test
    void testFindByIdPositive() {
        Product product = new Product();
        product.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product.setProductName("Laptop");
        product.setProductQuantity(12);
        productRepository.create(product);

        Product foundProduct = productRepository.findById("7b3961ae-953c-43d5-8825-b778e35d0982");

        assertNotNull(foundProduct);
        assertEquals("Laptop", foundProduct.getProductName());
        assertEquals(12, foundProduct.getProductQuantity());
    }

    @Test
    void testFindByIdNegativeNotFound() {
        Product foundProduct = productRepository.findById("non-existent-id");

        assertNull(foundProduct);
    }
}