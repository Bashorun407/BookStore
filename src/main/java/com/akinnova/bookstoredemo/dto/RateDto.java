package com.akinnova.bookstoredemo.dto;

import lombok.Data;

@Data
public class RateDto {
    private String username;
    private String title;
    private Integer starRating;
}
