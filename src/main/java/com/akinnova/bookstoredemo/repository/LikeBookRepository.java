package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.LikeBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeBookRepository extends JpaRepository<LikeBook, Long> {

    Boolean existsByTitle(String title);
    Optional<LikeBook> findByTitle(String title);
}
