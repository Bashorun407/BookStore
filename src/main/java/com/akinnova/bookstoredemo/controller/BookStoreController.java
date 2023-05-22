package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.ResponsePojo.BookResponsePojo;
import com.akinnova.bookstoredemo.dto.BookStoreDto;
import com.akinnova.bookstoredemo.entity.BookStore;
import com.akinnova.bookstoredemo.service.BookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstore")
public class BookStoreController {

    @Autowired
    BookStoreService bookStoreService;

    @PostMapping("/createbook")
    public BookResponsePojo<BookStore> createBook(@RequestBody BookStoreDto bookStoreDto){
        return bookStoreService.createBook(bookStoreDto);
    }

    @GetMapping("/findallbooks")
    public BookResponsePojo<List<BookStore>> findAllBooks(){
        return bookStoreService.findAllBooks();
    }

    @GetMapping("/findbyauthor/{author}")
    public BookResponsePojo<List<BookStore>> findBookByAuthor(@PathVariable String author){
        return bookStoreService.findBookByAuthor(author);
    }

    @GetMapping("/findbytitle/{title}")
    public BookResponsePojo<BookStore> findBookByTitle(@PathVariable String title){
        return bookStoreService.findBookByTitle(title);
    }

    @GetMapping("/findbygenre/{genre}")
    public BookResponsePojo<List<BookStore>> findBooksByGenre(@PathVariable String genre){
        return bookStoreService.findBooksByGenre(genre);
    }

    @GetMapping("/findbyserialnumber/{serialNumber}")
    public BookResponsePojo<BookStore> findBookBySerialNumber(@PathVariable Long serialNumber){
        return bookStoreService.findBookBySerialNumber(serialNumber);
    }

    @PutMapping("/updatebook")
    public BookResponsePojo<BookStore> updateBookContent(@RequestBody BookStoreDto bookStoreDto){
        return bookStoreService.updateBookContent(bookStoreDto);
    }

    @DeleteMapping("/deletebook/{id}")
    public BookResponsePojo<String> deleteBook(@PathVariable Long id){
        return bookStoreService.deleteBook(id);
    }
}
