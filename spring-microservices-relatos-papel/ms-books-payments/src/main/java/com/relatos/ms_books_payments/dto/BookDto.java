package com.relatos.ms_books_payments.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String isbn;
    private boolean visible;
    private Integer stock;
}
