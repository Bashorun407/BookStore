package com.akinnova.bookstoredemo.dto.ratedto;

import lombok.Data;

@Data
public class RateDto {
    private String username;
    private String title;
    private Integer starRating;
}
