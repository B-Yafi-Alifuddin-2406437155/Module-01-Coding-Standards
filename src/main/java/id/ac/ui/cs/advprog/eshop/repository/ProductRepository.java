package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {
    private final List<Product> productData = new ArrayList<>();

    public Product create(final Product product) {
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(final String productId) {
        for (final Product product : productData) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public void update(final Product updatedProduct) {
        final Product existingProduct = findById(updatedProduct.getProductId());
        if (existingProduct != null) {
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductQuantity(updatedProduct.getProductQuantity());
        }
    }

    public void deleteById(final String productId) {
        productData.removeIf(product -> product.getProductId().equals(productId));
    }
}