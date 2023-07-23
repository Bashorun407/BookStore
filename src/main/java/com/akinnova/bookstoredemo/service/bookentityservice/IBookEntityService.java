package com.akinnova.bookstoredemo.service.bookentityservice;

import com.akinnova.bookstoredemo.dto.bookentitydto.BookEntityDto;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookUpdateDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBookEntityService {
    ResponsePojo<BookEntity> createBook(BookEntityDto bookStoreDto);
    ResponseEntity<?> findAllBooks();
    ResponseEntity<?> findBookByAuthor(String author);
    ResponseEntity<?> findBookByTitle(String title);
    ResponseEntity<?> findBooksByGenre(String genre);
    ResponseEntity<?> findBookBySerialNumber(String serialNumber);
    ResponsePojo<BookEntity> updateBookContent(BookUpdateDto bookUpdateDto);
    ResponseEntity<?> deleteBook(String serialNumber);

    ResponseEntity<?> searchBook(String title, String author, String genre, String serialNumber, Pageable pageable);
}
