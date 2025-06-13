package com.relatos.ms_books_payments.service;

import com.relatos.ms_books_payments.client.BookClient;
import com.relatos.ms_books_payments.dto.BookDto;
import com.relatos.ms_books_payments.model.Purchase;
import com.relatos.ms_books_payments.repository.PurchaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class PurchaseService {

    private final PurchaseRepository repository;
    private final BookClient bookClient;

    public PurchaseService(PurchaseRepository repository, BookClient bookClient) {
        this.repository = repository;
        this.bookClient = bookClient;
    }

    public Purchase createPurchase(Purchase purchase) {
        BookDto book = validateBook(Long.valueOf(purchase.getBookId()));

        if (!book.isVisible()) {
            throw new IllegalStateException("El libro no está visible para la compra.");
        }

        if (book.getStock() == null || book.getStock() < purchase.getQuantity()) {
            throw new IllegalStateException("Stock insuficiente.");
        }

        if (purchase.getTotalPrice() == null) {
            throw new IllegalArgumentException("El precio total debe estar presente.");
        }

        return repository.save(purchase);
    }

    private BookDto validateBook(Long bookId) {
        try {
            return bookClient.getBookById(bookId).block();
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al comunicarse con el servicio catálogo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al validar libro con ID " + bookId + ": " + e.getMessage(), e);
        }
    }

    public Optional<Purchase> getPurchaseById(Long id) {
        return repository.findById(id);
    }
}
