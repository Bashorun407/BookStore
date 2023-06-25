package com.akinnova.bookstoredemo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookStoreDto {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private String summary;
    private Long serialNumber;
    private Integer volume;
    private Long quantity;
    private Double price;
    private LocalDateTime supplyDate;
    private Boolean deleteStatus;
}
