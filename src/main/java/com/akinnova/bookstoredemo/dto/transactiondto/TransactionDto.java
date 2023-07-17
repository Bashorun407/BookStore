package com.akinnova.bookstoredemo.dto.transactiondto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private String username;
    private Double amountPaid;
    private String invoiceCode;
    private LocalDateTime transactionDate;
}
