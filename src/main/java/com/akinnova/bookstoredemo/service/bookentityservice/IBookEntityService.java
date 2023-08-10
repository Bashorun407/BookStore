package com.akinnova.bookstoredemo.service.bookentityservice;

import com.akinnova.bookstoredemo.dto.bookentitydto.BookEntityDto;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookResponseDto;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookUpdateDto;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IBookEntityService {
    ResponsePojo<BookResponseDto> createBook(BookEntityDto bookStoreDto);
    ResponseEntity<?> findAllBooks(int pageNum, int pageSize);
    ResponseEntity<?> findBookByAuthor(String author, int pageNum, int pageSize);
    ResponseEntity<?> findBookByTitle(String title);
    ResponseEntity<?> findBooksByGenre(String genre, int pageNum, int pageSize);
    ResponseEntity<?> findBookBySerialNumber(String serialNumber);
    ResponseEntity<?> updateBookContent(BookUpdateDto bookUpdateDto);
    ResponseEntity<?> deleteBook(String serialNumber);

    ResponseEntity<?> searchBook(String title, String author, String genre, String serialNumber, Pageable pageable);
}
