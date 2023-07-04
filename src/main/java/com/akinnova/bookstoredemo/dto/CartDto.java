package com.akinnova.bookstoredemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDto {
    private String username;
    private String title;
    private String serialNumber;
    private Double price;
    private Integer quantity;
    private Double amountToPay;
    private Boolean checkOut = false;
}
