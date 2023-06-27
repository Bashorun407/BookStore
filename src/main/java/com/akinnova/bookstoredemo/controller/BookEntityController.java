package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.response.BookResponsePojo;
import com.akinnova.bookstoredemo.dto.BookEntityDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.service.BookEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookstore")
public class BookEntityController {

    @Autowired
   private BookEntityService bookStoreService;

    @PostMapping("/auth/createBook")
    public BookResponsePojo<BookEntity> createBook(@RequestBody BookEntityDto bookStoreDto){
        return bookStoreService.createBook(bookStoreDto);
    }

    @GetMapping("/auth/findAllBooks")
    public BookResponsePojo<List<BookEntity>> findAllBooks(){
        return bookStoreService.findAllBooks();
    }

    @GetMapping("/auth/findByAuthor/{author}")
    public BookResponsePojo<List<BookEntity>> findBookByAuthor(@PathVariable String author){
        return bookStoreService.findBookByAuthor(author);
    }

    @GetMapping("/auth/findByTitle/{title}")
    public BookResponsePojo<List<BookEntity>> findBookByTitle(@PathVariable String title){
        return bookStoreService.findBookByTitle(title);
    }

    @GetMapping("/auth/findByGenre/{genre}")
    public BookResponsePojo<List<BookEntity>> findBooksByGenre(@PathVariable String genre){
        return bookStoreService.findBooksByGenre(genre);
    }

    @GetMapping("/auth/findBySerialNumber/{serialNumber}")
    public BookResponsePojo<BookEntity> findBookBySerialNumber(@PathVariable String serialNumber){
        return bookStoreService.findBookBySerialNumber(serialNumber);
    }

    @PutMapping("/updateBook")
    public BookResponsePojo<BookEntity> updateBookContent(@RequestBody BookEntityDto bookStoreDto){
        return bookStoreService.updateBookContent(bookStoreDto);
    }

    @DeleteMapping("/deleteBook/{id}")
    public BookResponsePojo<String> deleteBook(@PathVariable Long id){
        return bookStoreService.deleteBook(id);
    }
}
