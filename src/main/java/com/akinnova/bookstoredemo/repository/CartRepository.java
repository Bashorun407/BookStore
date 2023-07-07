package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<List<Cart>> findByUsername(String username);
    Optional<Cart> findByTitle(String title);
    Optional<Cart> findBySerialNumber(String serialNumber);
}
