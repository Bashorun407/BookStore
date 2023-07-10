package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.RateBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RateBookRepository extends JpaRepository<RateBook, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByTitle(String title);
    Optional<List<RateBook>> findByUsername(String username);
    Optional<List<RateBook>> findByTitle(String title);
}
