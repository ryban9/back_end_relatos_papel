package com.relatos.ms_books_payments.controller;

import com.relatos.ms_books_payments.model.Purchase;
import com.relatos.ms_books_payments.service.PurchaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody Purchase purchase) {
        try {
            Purchase savedPurchase = purchaseService.createPurchase(purchase);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Long id) {
        Optional<Purchase> purchase = purchaseService.getPurchaseById(id);
        if (purchase.isPresent()) {
            return ResponseEntity.ok(purchase.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Compra no encontrada con ID " + id);
        }
    }

}
