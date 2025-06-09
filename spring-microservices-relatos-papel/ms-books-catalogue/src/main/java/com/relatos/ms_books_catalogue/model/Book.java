package com.relatos.ms_books_catalogue.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    private String category;

    @Column(unique = true)
    private String isbn;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Boolean visible;
}
