package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    Optional<BookReview> findByBookStore(String title);
    Optional<BookReview> findBySerialNumber(String serialNumber);
}
