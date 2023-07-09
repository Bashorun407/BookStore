package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.LikeDto;
import com.akinnova.bookstoredemo.dto.RateDto;
import com.akinnova.bookstoredemo.dto.ReviewDto;
import com.akinnova.bookstoredemo.entity.Review;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReviewService {
   // ResponseEntity<?> reviewBook(ReviewDto reviewDto);
    ResponseEntity<?> likeBook(LikeDto likeDto);
    ResponseEntity<?> rateBook(RateDto rateDto);

    ResponsePojo<List<Review>> titleReviews(String title);
    ResponsePojo<List<Review>> allReviews();
}
