package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByUsername(String username);
    Optional<List<Transaction>> findByInvoiceCode(String invoiceCode);
}
