package com.akinnova.bookstoredemo.service.transactionservice;
import com.akinnova.bookstoredemo.dto.transactiondto.CartItemPurchaseDto;
import com.akinnova.bookstoredemo.entity.Transaction;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ITransactionService {
    ResponsePojo<Transaction> cashPayment(CartItemPurchaseDto cartItemPurchaseDto);
    ResponseEntity<?> transactionByUsername(String username);
    ResponseEntity<?> transactionByInvoiceCode(String transactionCode);
    ResponseEntity<?> searchTransaction(String username, String invoiceCode, Pageable pageable);
}

