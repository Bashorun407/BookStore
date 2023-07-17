package com.akinnova.bookstoredemo.service.reviewservice;

import com.akinnova.bookstoredemo.dto.likedto.LikeDto;
import com.akinnova.bookstoredemo.dto.ratedto.RateDto;
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
