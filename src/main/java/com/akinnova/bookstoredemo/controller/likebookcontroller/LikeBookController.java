package com.akinnova.bookstoredemo.controller.likebookcontroller;

import com.akinnova.bookstoredemo.dto.likedto.LikeDto;
import com.akinnova.bookstoredemo.entity.LikeBook;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.likebook.LikeBookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes/auth")
public class LikeBookController {
    private final LikeBookServiceImpl likeBookService;

    //Class constructor
    public LikeBookController(LikeBookServiceImpl likeBookService) {
        this.likeBookService = likeBookService;
    }

    //1) Method to like book
    @PostMapping("/like")
    public ResponseEntity<?> likeBook(@RequestBody LikeDto likeDto) {
        return likeBookService.likeBook(likeDto);
    }

    //2) Method to retrieve likes on specific book title
    @GetMapping("/titleLikes/{title}")
    public ResponseEntity<?> titleLikes(@PathVariable(name = "title") String title) {
        return likeBookService.titleLikes(title);
    }

    //3) Method to retrieve all book-likes in the database
    @GetMapping("/allLikes")
    public ResponseEntity<?> allReviews() {
        return likeBookService.allLikes();
    }
}
