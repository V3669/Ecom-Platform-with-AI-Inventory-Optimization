package com.ecom.inventory.service;

import com.ecom.inventory.model.Inventory;
import com.ecom.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Optional<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    public Inventory saveInventory(Inventory inventory) {
        inventory.setLastUpdated(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public boolean reserveStock(Long productId, Integer quantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);
        
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            int availableQuantity = inventory.getQuantity() - inventory.getReservedQuantity();
            
            if (availableQuantity >= quantity) {
                inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
                inventory.setLastUpdated(LocalDateTime.now());
                inventoryRepository.save(inventory);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean releaseStock(Long productId, Integer quantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);
        
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            
            if (inventory.getReservedQuantity() >= quantity) {
                inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
                inventory.setLastUpdated(LocalDateTime.now());
                inventoryRepository.save(inventory);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }

    @Override
    public boolean isInStock(Long productId, Integer quantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);
        
        return inventoryOptional.map(inventory -> 
            (inventory.getQuantity() - inventory.getReservedQuantity()) >= quantity
        ).orElse(false);
    }
} 