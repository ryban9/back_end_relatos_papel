package com.relatos.ms_books_catalogue.service;

import com.relatos.ms_books_catalogue.Specification.BookSpecification;
import com.relatos.ms_books_catalogue.dto.BookSearchCriteria;
import com.relatos.ms_books_catalogue.model.Book;
import com.relatos.ms_books_catalogue.repository.BookRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca libros aplicando filtros dinámicos combinables.
     */
    public List<Book> searchBooks(BookSearchCriteria criteria) {
        Specification<Book> spec = Specification.where(null);

        if (criteria.getTitle() != null)
            spec = spec.and(BookSpecification.hasTitle(criteria.getTitle()));
        if (criteria.getAuthor() != null)
            spec = spec.and(BookSpecification.hasAuthor(criteria.getAuthor()));
        if (criteria.getCategory() != null)
            spec = spec.and(BookSpecification.hasCategory(criteria.getCategory()));
        if (criteria.getIsbn() != null)
            spec = spec.and(BookSpecification.hasIsbn(criteria.getIsbn()));
        if (criteria.getRating() != null)
            spec = spec.and(BookSpecification.hasRating(criteria.getRating()));
        if (criteria.getVisible() != null)
            spec = spec.and(BookSpecification.isVisible(criteria.getVisible()));
        if (criteria.getPublishedAfter() != null)
            spec = spec.and(BookSpecification.publishedAfter(criteria.getPublishedAfter()));

        return repository.findAll(spec);
    }

    /**
     * Crea un nuevo libro validando los datos obligatorios.
     */
    public Book createBook(Book book) {
        validateBook(book);
        return repository.save(book);
    }

    /**
     * Obtiene un libro por ID, devuelve Optional vacío si no existe.
     */
    public Optional<Book> getBookById(Long id) {
        return repository.findById(id);
    }

    /**
     * Actualiza completamente un libro por ID.
     */
    public Optional<Book> updateBook(Long id, Book book) {
        return repository.findById(id).map(existing -> {
            validateBook(book);
            existing.setTitle(book.getTitle());
            existing.setAuthor(book.getAuthor());
            existing.setCategory(book.getCategory());
            existing.setIsbn(book.getIsbn());
            existing.setRating(book.getRating());
            existing.setVisible(book.getVisible());
            existing.setPublicationDate(book.getPublicationDate());
            return repository.save(existing);
        });
    }

    /**
     * Actualización parcial de un libro por ID con un mapa de campos a actualizar.
     */
    public Optional<Book> partialUpdateBook(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(existing -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "title":
                        String title = (String) value;
                        if (title == null || title.isBlank()) {
                            throw new IllegalArgumentException("El título es obligatorio");
                        }
                        existing.setTitle(title);
                        break;

                    case "author":
                        String author = (String) value;
                        if (author == null || author.isBlank()) {
                            throw new IllegalArgumentException("El autor es obligatorio");
                        }
                        existing.setAuthor(author);
                        break;

                    case "category":
                        existing.setCategory((String) value);
                        break;

                    case "isbn":
                        String isbn = (String) value;
                        if (isbn == null || isbn.isBlank()) {
                            throw new IllegalArgumentException("El ISBN es obligatorio");
                        }
                        existing.setIsbn(isbn);
                        break;

                    case "rating":
                        Integer rating = convertToInteger(value);
                        if (rating != null && (rating < 1 || rating > 5)) {
                            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
                        }
                        existing.setRating(rating);
                        break;

                    case "visible":
                        existing.setVisible((Boolean) value);
                        break;

                    case "publicationDate":
                        if (value instanceof String) {
                            existing.setPublicationDate(LocalDate.parse((String) value));
                        }
                        break;
                }
            });
            validateBook(existing); // Validar el libro después de aplicar cambios parciales
            return repository.save(existing);
        });
    }

    /**
     * Elimina un libro por ID, devuelve true si existía y fue eliminado.
     */
    public boolean deleteBook(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }


    /**
     * Valida un libro para asegurar que los campos obligatorios y reglas se cumplen.
     */
    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("El autor es obligatorio");
        }
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("El ISBN es obligatorio");
        }
        if (book.getRating() != null && (book.getRating() < 1 || book.getRating() > 5)) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
    }

    /**
     * Convierte un objeto en Integer de forma segura.
     */
    private Integer convertToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException ignored) {}
        }
        throw new IllegalArgumentException("Valor no válido para calificación");
    }
}
