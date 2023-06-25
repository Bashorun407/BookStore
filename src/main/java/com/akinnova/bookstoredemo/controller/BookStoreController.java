package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.response.BookResponsePojo;
import com.akinnova.bookstoredemo.dto.BookStoreDto;
import com.akinnova.bookstoredemo.entity.BookStore;
import com.akinnova.bookstoredemo.service.BookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookstore")
public class BookStoreController {

    @Autowired
   private BookStoreService bookStoreService;

    @PostMapping("/auth/createBook")
    public BookResponsePojo<BookStore> createBook(@RequestBody BookStoreDto bookStoreDto){
        return bookStoreService.createBook(bookStoreDto);
    }

    @GetMapping("/auth/findAllBooks")
    public BookResponsePojo<List<BookStore>> findAllBooks(){
        return bookStoreService.findAllBooks();
    }

    @GetMapping("/auth/findByAuthor/{author}")
    public BookResponsePojo<List<BookStore>> findBookByAuthor(@PathVariable String author){
        return bookStoreService.findBookByAuthor(author);
    }

    @GetMapping("/auth/findByTitle/{title}")
    public BookResponsePojo<List<BookStore>> findBookByTitle(@PathVariable String title){
        return bookStoreService.findBookByTitle(title);
    }

    @GetMapping("/auth/findByGenre/{genre}")
    public BookResponsePojo<List<BookStore>> findBooksByGenre(@PathVariable String genre){
        return bookStoreService.findBooksByGenre(genre);
    }

    @GetMapping("/auth/findBySerialNumber/{serialNumber}")
    public BookResponsePojo<BookStore> findBookBySerialNumber(@PathVariable String serialNumber){
        return bookStoreService.findBookBySerialNumber(serialNumber);
    }

    @PutMapping("/updateBook")
    public BookResponsePojo<BookStore> updateBookContent(@RequestBody BookStoreDto bookStoreDto){
        return bookStoreService.updateBookContent(bookStoreDto);
    }

    @DeleteMapping("/deleteBook/{id}")
    public BookResponsePojo<String> deleteBook(@PathVariable Long id){
        return bookStoreService.deleteBook(id);
    }
}
