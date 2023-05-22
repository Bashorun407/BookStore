package com.akinnova.bookstoredemo.dto;

import lombok.Data;

import java.util.Date;

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
    private Date supplyDate;
    private Boolean deleteStatus;
}
