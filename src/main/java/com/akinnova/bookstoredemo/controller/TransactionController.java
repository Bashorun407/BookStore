package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.dto.CartItemPurchaseDto;
import com.akinnova.bookstoredemo.dto.CustomerCardDto;
import com.akinnova.bookstoredemo.entity.Transaction;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction/auth/")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;
    //1) Method to conduct transaction
    @PostMapping("/transaction")
    public ResponsePojo<Transaction> cashPayment(@RequestBody CartItemPurchaseDto cartItemPurchaseDto) {
        return transactionService.cashPayment(cartItemPurchaseDto);
    }

}
