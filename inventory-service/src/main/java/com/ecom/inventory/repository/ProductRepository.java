package com.ecom.inventory.repository;

import com.ecom.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByStockQuantityLessThan(Integer threshold);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
} 