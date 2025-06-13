package com.relatos.ms_books_payments.client;

import com.relatos.ms_books_payments.dto.BookDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {

    private final WebClient webClient;

    public BookClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://ms-books-catalogue/api/books")
                .build();
    }

    public Mono<BookDto> getBookById(Long bookId) {
        return webClient.get()
                .uri("/{id}", bookId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> Mono.error(new EntityNotFoundException("Libro no encontrado con ID " + bookId)))
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Error en el servicio catálogo")))
                .bodyToMono(BookDto.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)));
    }
}
