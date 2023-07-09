package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.dto.LikeDto;
import com.akinnova.bookstoredemo.dto.RateDto;
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
    private final ReviewServiceImpl reviewService;

    //Class Constructor
    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    //1) Method to like book
    @PostMapping("/like")
    public ResponseEntity<?> likeBook(@RequestBody LikeDto likeDto) {
        return reviewService.likeBook(likeDto);
    }

    //2) Method to rate book...(Rate should be between 1 and 5)
    @PostMapping("/rate")
    public ResponseEntity<?> rateBook(@RequestBody RateDto rateDto) {
        return reviewService.rateBook(rateDto);
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

    //1) Method to review book
//    @PostMapping("/review")
//    public ResponseEntity<?> reviewBook(@RequestBody ReviewDto reviewDto) {
//        return reviewService.reviewBook(reviewDto);
//    }

}
