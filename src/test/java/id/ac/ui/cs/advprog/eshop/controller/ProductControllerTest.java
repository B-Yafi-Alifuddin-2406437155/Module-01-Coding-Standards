package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("7b3961ae-953c-43d5-8825-b778e35d0982");
        product.setProductName("Laptop");
        product.setProductQuantity(12);
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);

        assertEquals("CreateProduct", viewName);
        verify(model, times(1)).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost() {
        when(productService.create(product)).thenReturn(product);

        String viewName = productController.createProductPost(product, model);

        assertEquals("redirect:/product/list", viewName);
        verify(productService, times(1)).create(product);
    }

    @Test
    void testProductListPage() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productService.findAll()).thenReturn(productList);

        String viewName = productController.productListPage(model);

        assertEquals("ProductList", viewName);
        verify(model, times(1)).addAttribute("products", productList);
        verify(productService, times(1)).findAll();
    }

    @Test
    void testEditProductPage() {
        when(productService.findById("7b3961ae-953c-43d5-8825-b778e35d0982")).thenReturn(product);

        String viewName = productController.editProductPage("7b3961ae-953c-43d5-8825-b778e35d0982", model);

        assertEquals("EditProduct", viewName);
        verify(model, times(1)).addAttribute("product", product);
        verify(productService, times(1)).findById("7b3961ae-953c-43d5-8825-b778e35d0982");
    }

    @Test
    void testEditProductPost() {
        doNothing().when(productService).update(product);

        String viewName = productController.editProductPost(product);

        assertEquals("redirect:/product/list", viewName);
        verify(productService, times(1)).update(product);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteById("7b3961ae-953c-43d5-8825-b778e35d0982");

        String viewName = productController.deleteProduct("7b3961ae-953c-43d5-8825-b778e35d0982");

        assertEquals("redirect:/product/list", viewName);
        verify(productService, times(1)).deleteById("7b3961ae-953c-43d5-8825-b778e35d0982");
    }
}