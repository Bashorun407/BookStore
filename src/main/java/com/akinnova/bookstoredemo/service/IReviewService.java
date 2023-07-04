package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.ReviewDto;
import com.akinnova.bookstoredemo.entity.Review;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReviewService {
    ResponseEntity<?> reviewBook(ReviewDto reviewDto);

    ResponsePojo<List<Review>> titleReviews(String title);
    ResponsePojo<List<Review>> allReviews();
}
