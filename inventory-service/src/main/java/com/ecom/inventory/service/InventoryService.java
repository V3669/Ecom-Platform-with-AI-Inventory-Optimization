package com.ecom.inventory.service;

import com.ecom.inventory.model.Inventory;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> getAllInventory();
    Optional<Inventory> getInventoryById(Long id);
    Optional<Inventory> getInventoryByProductId(Long productId);
    Inventory saveInventory(Inventory inventory);
    boolean reserveStock(Long productId, Integer quantity);
    boolean releaseStock(Long productId, Integer quantity);
    List<Inventory> getLowStockItems();
    boolean isInStock(Long productId, Integer quantity);
} 