package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.BookEntityDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.service.BookEntityServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookstore")
public class BookEntityController {

    @Autowired
   private BookEntityServiceImpl bookStoreService;

    @PostMapping("/auth/createBook")
    public ResponsePojo<BookEntity> createBook(@RequestBody BookEntityDto bookStoreDto){
        return bookStoreService.createBook(bookStoreDto);
    }

    @GetMapping("/auth/allBooks")
    public ResponsePojo<List<BookEntity>> findAllBooks(){
        return bookStoreService.findAllBooks();
    }

    @GetMapping("/auth/author/{author}")
    public ResponsePojo<List<BookEntity>> findBookByAuthor(@PathVariable String author){
        return bookStoreService.findBookByAuthor(author);
    }

    @GetMapping("/auth/title/{title}")
    public ResponsePojo<BookEntity> findBookByTitle(@PathVariable String title){
        return bookStoreService.findBookByTitle(title);
    }

    @GetMapping("/auth/genre/{genre}")
    public ResponsePojo<List<BookEntity>> findBooksByGenre(@PathVariable String genre){
        return bookStoreService.findBooksByGenre(genre);
    }

    @GetMapping("/auth/serialNumber/{serialNumber}")
    public ResponsePojo<BookEntity> findBookBySerialNumber(@PathVariable String serialNumber){
        return bookStoreService.findBookBySerialNumber(serialNumber);
    }

    @PutMapping("/book")
    public ResponsePojo<BookEntity> updateBookContent(@RequestBody BookEntityDto bookStoreDto){
        return bookStoreService.updateBookContent(bookStoreDto);
    }

    @DeleteMapping("/book/{id}")
    public ResponsePojo<String> deleteBook(@PathVariable Long id){
        return bookStoreService.deleteBook(id);
    }

    //9) Method to search book by multiple parameters
    @GetMapping("auth/search")
    public ResponsePojo<Page<BookEntity>> searchBook(@RequestParam(name = "title", required = false) String title,
                                                     @RequestParam(name = "author", required = false) String author,
                                                     @RequestParam(name = "genre", required = false) String genre,
                                                     @RequestParam(name = "serialNumber", required = false) String serialNumber,
                                                     Pageable pageable) {
        return bookStoreService.searchBook(title, author, genre, serialNumber, pageable);
    }
}
