package com.ecom.inventory.controller;

import com.ecom.inventory.model.Inventory;
import com.ecom.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.saveInventory(inventory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        return inventoryService.getInventoryById(id)
                .map(existingInventory -> {
                    inventory.setId(id);
                    return ResponseEntity.ok(inventoryService.saveInventory(inventory));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reserve")
    public ResponseEntity<Map<String, Boolean>> reserveStock(
            @RequestParam Long productId, 
            @RequestParam Integer quantity) {
        boolean reserved = inventoryService.reserveStock(productId, quantity);
        return ResponseEntity.ok(Map.of("reserved", reserved));
    }

    @PostMapping("/release")
    public ResponseEntity<Map<String, Boolean>> releaseStock(
            @RequestParam Long productId, 
            @RequestParam Integer quantity) {
        boolean released = inventoryService.releaseStock(productId, quantity);
        return ResponseEntity.ok(Map.of("released", released));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/in-stock")
    public ResponseEntity<Map<String, Boolean>> checkInStock(
            @RequestParam Long productId, 
            @RequestParam Integer quantity) {
        boolean inStock = inventoryService.isInStock(productId, quantity);
        return ResponseEntity.ok(Map.of("inStock", inStock));
    }
} 