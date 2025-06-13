package com.relatos.ms_books_catalogue.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookSearchCriteria {
    private String title;
    private String author;
    private String category;
    private String isbn;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer rating;

    private Boolean visible;

    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate publishedAfter;

    private Integer stock;
}
