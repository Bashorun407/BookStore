package com.akinnova.bookstoredemo.dto.cartdto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CustomerCardDto {
    private String name;
    private String atmNumber;
    private String atmPin;
    private Double balance;
}
