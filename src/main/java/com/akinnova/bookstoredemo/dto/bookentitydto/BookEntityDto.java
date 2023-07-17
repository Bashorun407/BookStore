package com.akinnova.bookstoredemo.dto.bookentitydto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookEntityDto {
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
