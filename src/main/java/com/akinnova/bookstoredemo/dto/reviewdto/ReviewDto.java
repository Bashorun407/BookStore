package com.akinnova.bookstoredemo.dto.reviewdto;

import lombok.Data;

@Data
public class ReviewDto {
    private String username;
    private String title;
    private Boolean likes;
    private Integer starRating;
//    private Long totalLikes;
//    private Integer averageRating;
}
