package com.ecom.inventory.service;

import com.ecom.inventory.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    List<Product> getProductsByCategory(String category);
    List<Product> searchProducts(String keyword);
    List<Product> getLowStockProducts(Integer threshold);
} 