package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.dto.ReviewDto;
import com.akinnova.bookstoredemo.entity.Review;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review/auth")
public class ReviewController {

    @Autowired
    private ReviewServiceImpl reviewService;
    //1) Method to review book
    @PostMapping("/review")
    public ResponseEntity<?> reviewBook(@RequestBody ReviewDto reviewDto) {
        return reviewService.reviewBook(reviewDto);
    }

    //2) Method to retrieve reviews on specific book title
    @GetMapping("/titleReviews/{title}")
    public ResponsePojo<List<Review>> titleReviews(@PathVariable String title) {
        return reviewService.titleReviews(title);
    }

    //3) Method to retrieve all reviews in the database
    @GetMapping("/allReviews")
    public ResponsePojo<List<Review>> allReviews() {
        return reviewService.allReviews();
    }
}
