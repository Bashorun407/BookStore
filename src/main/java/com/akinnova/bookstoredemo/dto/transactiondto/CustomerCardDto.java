package com.akinnova.bookstoredemo.dto.transactiondto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CustomerCardDto {
    private String firstName;
    private String lastName;
    private String atmNumber;
    private String atmPin;
    private Double balance;
}
