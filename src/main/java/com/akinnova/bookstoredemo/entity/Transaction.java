package com.akinnova.bookstoredemo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private BookEntity book;
    private Customer customer;
    private BigDecimal amount;
    private String transactionCode;
    private LocalDateTime transactionDate;
    // TODO: 6/27/2023 (Dispatcher can be mapped into this class later) 
    private String dispatcher;
    
}
