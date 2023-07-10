package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.dto.CartItemPurchaseDto;
import com.akinnova.bookstoredemo.entity.Transaction;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction/auth")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    //1) Method to conduct transaction
    @PostMapping("/transaction")
    public ResponsePojo<Transaction> cashPayment(@RequestBody CartItemPurchaseDto cartItemPurchaseDto) {
        return transactionService.cashPayment(cartItemPurchaseDto);
    }

    //2) Method to find transaction details by username
    @GetMapping("/transactionUser/{username}")
    public ResponsePojo<List<Transaction>> transactionByUsername(@PathVariable(name = "username") String username) {
        return transactionService.transactionByUsername(username);
    }

    //3) Method to find transaction details by invoice code
    @GetMapping("/transactionCode/{invoiceCode}")
    public ResponsePojo<Transaction> transactionByInvoiceCode(@PathVariable(name = "invoiceCode") String transactionCode) {
        return transactionService.transactionByInvoiceCode(transactionCode);
    }

    //4) Method to search for transaction details with different parameters
    @GetMapping("/search")
    public ResponsePojo<Page<Transaction>> searchTransaction(@RequestParam(name = "username", required = false) String username,
                                                             @RequestParam(name = "invoiceCode") String invoiceCode,
                                                             Pageable pageable) {
        return transactionService.searchTransaction(username, invoiceCode, pageable);
    }

}
