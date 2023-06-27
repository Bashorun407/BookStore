package com.akinnova.bookstoredemo.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BookReviewDto {
    private Long like;
    private Long star;
    //Implementation of comment may be with a List or Map
    // TODO: 6/27/2023 (using List or Map for comment)
    private String comment;
    private LocalDateTime reviewDate;

}
