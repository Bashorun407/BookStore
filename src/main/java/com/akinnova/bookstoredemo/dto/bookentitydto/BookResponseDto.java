package com.akinnova.bookstoredemo.dto.bookentitydto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponseDto {
    private String imageAddress;
    private String title;
    private String author;
    private String summary;
    private Double price;
}
