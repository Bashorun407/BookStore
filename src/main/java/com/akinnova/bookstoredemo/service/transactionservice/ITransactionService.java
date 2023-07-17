package com.akinnova.bookstoredemo.service.transactionservice;
import com.akinnova.bookstoredemo.dto.cartdto.CartItemPurchaseDto;
import com.akinnova.bookstoredemo.entity.Transaction;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITransactionService {
    ResponsePojo<Transaction> cashPayment(CartItemPurchaseDto cartItemPurchaseDto);
    ResponsePojo<List<Transaction>> transactionByUsername(String username);
    ResponsePojo<Transaction> transactionByInvoiceCode(String transactionCode);
    ResponsePojo<Page<Transaction>> searchTransaction(String username, String invoiceCode, Pageable pageable);
}
