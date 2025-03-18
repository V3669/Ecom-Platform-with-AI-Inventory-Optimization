package com.ecom.ai.controller;

import com.ecom.ai.service.InventoryOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {
    private final InventoryOptimizationService inventoryOptimizationService;

    @GetMapping("/inventory/recommendations/{productId}")
    public ResponseEntity<Map<String, Object>> getProductRecommendations(
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryOptimizationService.getProductRecommendations(productId));
    }

    @PostMapping("/inventory/optimize")
    public ResponseEntity<Void> triggerInventoryOptimization() {
        inventoryOptimizationService.optimizeInventory();
        return ResponseEntity.ok().build();
    }
} 