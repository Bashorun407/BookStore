package com.akinnova.bookstoredemo.controller.bookentitycontroller;

import com.akinnova.bookstoredemo.dto.bookentitydto.BookResponseDto;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookUpdateDto;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookEntityDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.service.bookentityservice.BookEntityServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/swagger-ui/index.html/api/v1/bookstore")
public class BookEntityController {

    @Autowired
   private BookEntityServiceImpl bookStoreService;

    @PostMapping("/auth/createBook")
    public ResponsePojo<BookResponseDto> createBook(@RequestBody BookEntityDto bookStoreDto){
        return bookStoreService.createBook(bookStoreDto);
    }

    @GetMapping("/auth/allBooks")
    public ResponseEntity<?> findAllBooks(@RequestParam(name = "pageNum") int pageNum, @RequestParam(name = "pageSize") int pageSize){
        return bookStoreService.findAllBooks(pageNum, pageSize);
    }

    @GetMapping("/auth/author/{author}")
    public ResponseEntity<?> findBookByAuthor(@PathVariable String author, @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize){
        return bookStoreService.findBookByAuthor(author, pageNum, pageSize);
    }

    @GetMapping("/auth/title/{title}")
    public ResponseEntity<?> findBookByTitle(@PathVariable String title){
        return bookStoreService.findBookByTitle(title);
    }

    @GetMapping("/auth/genre/{genre}")
    public ResponseEntity<?> findBooksByGenre(@PathVariable String genre, @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize){
        return bookStoreService.findBooksByGenre(genre, pageNum, pageSize);
    }

    @GetMapping("/auth/serialNumber/{serialNumber}")
    public ResponseEntity<?> findBookBySerialNumber(@PathVariable String serialNumber){
        return bookStoreService.findBookBySerialNumber(serialNumber);
    }

    @PutMapping("/auth/book")
    public ResponseEntity<BookResponseDto> updateBookContent(@RequestBody BookUpdateDto bookUpdateDto){
        return bookStoreService.updateBookContent(bookUpdateDto);
    }

    @DeleteMapping("/auth/book/{serialNumber}")
    public ResponseEntity<?> deleteBook(@PathVariable String serialNumber){
        return bookStoreService.deleteBook(serialNumber);
    }

    //9) Method to search book by multiple parameters
    @GetMapping("auth/search")
    public ResponseEntity<?> searchBook(@RequestParam(name = "title", required = false) String title,
                                                     @RequestParam(name = "author", required = false) String author,
                                                     @RequestParam(name = "genre", required = false) String genre,
                                                     @RequestParam(name = "serialNumber", required = false) String serialNumber,
                                                    @RequestParam Pageable pageable) {
        return bookStoreService.searchBook(title, author, genre, serialNumber, pageable);
    }
}
