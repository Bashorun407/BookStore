package com.akinnova.bookstoredemo.dto.cartdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponseDto {
    private String imageAddress;
    private String title;
    private String cartItemNumber;
    private Double price;
    private Integer quantity;
    private Boolean checkOut;
}
