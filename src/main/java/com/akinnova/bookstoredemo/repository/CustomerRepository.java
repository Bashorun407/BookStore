package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsByUsernameOrEmail(String username, String email);
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByEmail(String email);
}
