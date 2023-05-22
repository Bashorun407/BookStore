package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.BookStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookStoreRepository extends JpaRepository<BookStore, Long> {

    //defining some methods to query the repository
    Optional<List<BookStore>> findBookByAuthor(String author);
    Optional<BookStore> findBookByTitle(String title);
    Optional<List<BookStore>> findBooksByGenre(String genre);
    Optional<BookStore> findBookBySerialNumber(Long serialNumber);
}
