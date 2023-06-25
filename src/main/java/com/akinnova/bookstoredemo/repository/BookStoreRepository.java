package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.BookStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookStoreRepository extends JpaRepository<BookStore, Long> {

    //defining some methods to query the repository
    @Query("select (count(u) > 0) from BookStore u where u.title = ?1 and u.volume = ?1")
    Boolean existsByTitleAndVolume(String title, Integer volume);
    Optional<List<BookStore>> findBookByAuthor(String author);
    Optional<List<BookStore>> findBookByTitle(String title);
    Optional<List<BookStore>> findBooksByGenre(String genre);
    Optional<BookStore> findBookBySerialNumber(String serialNumber);
}
