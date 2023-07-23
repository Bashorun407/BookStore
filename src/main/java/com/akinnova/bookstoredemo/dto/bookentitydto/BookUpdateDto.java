package com.akinnova.bookstoredemo.dto.bookentitydto;

import lombok.Data;

@Data
public class BookUpdateDto {
    private String imageAddress;
    private String serialNumber;
    private String summary;
    private String edition;
    private Long quantity;
    private Double price;
    private Boolean deleteStatus;
}
