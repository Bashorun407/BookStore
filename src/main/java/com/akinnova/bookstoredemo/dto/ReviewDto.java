package com.akinnova.bookstoredemo.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private String username;
    private String title;
    private Integer likes;
    private Integer starRating;
//    private Long totalLikes;
//    private Integer averageRating;
}
