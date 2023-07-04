package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookEntityRepository extends JpaRepository<BookEntity, Long> {

    Boolean existsByTitle(String title);
    Optional<List<BookEntity>> findBookByAuthor(String author);
    Optional<List<BookEntity>> findBookByTitle(String title);
    Optional<List<BookEntity>> findBooksByGenre(String genre);
    Optional<BookEntity> findBookBySerialNumber(String serialNumber);
}
