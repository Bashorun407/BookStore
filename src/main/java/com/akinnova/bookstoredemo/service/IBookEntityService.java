package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.BookEntityDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.response.ResponsePojo;

import java.util.List;

public interface IBookEntityService {
    ResponsePojo<BookEntity> createBook(BookEntityDto bookStoreDto);
    ResponsePojo<List<BookEntity>> findAllBooks();
    ResponsePojo<List<BookEntity>> findBookByAuthor(String author);
    ResponsePojo<BookEntity> findBookByTitle(String title);
    ResponsePojo<List<BookEntity>> findBooksByGenre(String genre);
    ResponsePojo<BookEntity> findBookBySerialNumber(String serialNumber);
    ResponsePojo<BookEntity> updateBookContent(BookEntityDto bookStoreDto);
    ResponsePojo<String> deleteBook(Long id);
}
