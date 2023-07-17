package com.akinnova.bookstoredemo.dto.cartdto;

import lombok.Data;

@Data
public class CartItemPurchaseDto {
    private String username;
    private String name;
    private String email;
    private Double balance;
    private Boolean checkOut = true;
}
