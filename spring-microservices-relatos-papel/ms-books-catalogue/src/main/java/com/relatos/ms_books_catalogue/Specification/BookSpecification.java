package com.relatos.ms_books_catalogue.Specification;

import com.relatos.ms_books_catalogue.model.Book;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BookSpecification {
    public static Specification<Book> hasTitle(String title) {
        return (root, query, builder) ->
                title == null ? null : builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, builder) ->
                author == null ? null : builder.like(builder.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> hasCategory(String category) {
        return (root, query, builder) ->
                category == null ? null : builder.equal(root.get("category"), category);
    }

    public static Specification<Book> hasIsbn(String isbn) {
        return (root, query, builder) ->
                isbn == null ? null : builder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> hasRating(Integer rating) {
        return (root, query, builder) ->
                rating == null ? null : builder.equal(root.get("rating"), rating);
    }

    public static Specification<Book> isVisible(Boolean visible) {
        return (root, query, builder) ->
                visible == null ? null : builder.equal(root.get("visible"), visible);
    }

    public static Specification<Book> publishedAfter(LocalDate date) {
        return (root, query, builder) ->
                date == null ? null : builder.greaterThanOrEqualTo(root.get("publicationDate"), date);
    }

    public static Specification<Book> hasStock(Integer stock) {
        return (root, query, cb) -> cb.equal(root.get("stock"), stock);
    }
}
