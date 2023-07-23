package com.akinnova.bookstoredemo.dto.transactiondto;

import lombok.Data;

@Data
public class CartItemPurchaseDto {
    private String username;
    private Double balance;
}
