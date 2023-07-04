package com.akinnova.bookstoredemo.service;
import com.akinnova.bookstoredemo.dto.CartItemPurchaseDto;
import com.akinnova.bookstoredemo.entity.Transaction;
import com.akinnova.bookstoredemo.response.ResponsePojo;

import java.util.List;

public interface ITransactionService {
    ResponsePojo<Transaction> cashPayment(CartItemPurchaseDto cartItemPurchaseDto);
}
